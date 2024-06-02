/* global doT */

class Field {
    constructor() {
        this.validators = [];
        this.changeListener = [];
        this.parent = null;
        this.id = null;
    }
    validate() {
        let v = this.normalize(this.value);
        for (let i = 0; i < this.validators.length; i++) {
            this.clearErrorMessage();
            let validator = this.validators[i]
            ,
                    [success, message] = validator.valid(v);
            if (!success) {
                this.setErrorMessage(message);
                return false;
            }
        }
        return true;
    }
    addValidator(v) {
        this.validators.push(v);
        return this;
    }
    setErrorMessage() {
        // nop
    }
    clearErrorMessage() {
        // nop		
    }
    change(e) {
        this.validate();
        for (let i = 0; i < this.changeListener.length; i++) {
            this.changeListener[i]();
        }
        if (this.parent !== null) {
            this.parent.change(e);
        }
    }
    addChangeListener(l) {
        this.changeListener.push(l);
        return this;
    }
    normalize(v) {
        return v;
    }
    setParent(parent) {
        this.parent = parent;
        return this;
    }
    getElement() {
        return this.element;
    }
}
class InputField extends Field {

    constructor(conf) {
        conf = {...{clazz: '', placeholder: '', type: 'text'}, ...conf}
        super();
        let template = doT.template(document.querySelector('#dot-input').innerHTML);
        this.element = html2Element(template(conf));
        this.input = this.element.querySelector('input');
        this.error = this.element.querySelector('.error');
        this.input.addEventListener("change", () => this.change());
        this.addChangeListener(() => {
            this.value = this.value; // normalize
        });
    }
    get value() {
        return this.normalize(this.input.value);
    }
    set value(x) {
        this.input.value = x;
    }
    setErrorMessage(message) {
        this.element.classList.add('hasError');
        this.error.innerHTML = message;
    }
    clearErrorMessage() {
        this.element.classList.remove('hasError');
        this.error.innerHTML = "";
    }
    normalize(v) {
        return v.replace(/\s+/g, ' ').trim();
    }
    isBlanc() {
        return this.normalize(this.input.value) == '';
    }

}

class ArrayFiled extends Field {
    constructor(conf, root, childBuilder, empty) {
        super();
        this.root = root;
        this.empty = empty;
        this.placeholder = root.innerHTML;
        this.childBuilder = childBuilder;
        this.lastChild = null;
        this.children = [];
    }
    placeholderOn() {
        this.root.innerHTML = this.placeholder;
    }

    get value() {
        var ret = [];
        for (var i = 0; i < this.children.length; i++) {
            let ch = this.children[i];
            if (!ch.isBlanc()) {
                ret.push(ch.value);
            }
        }
        return this.normalize(ret);
    }
    set value(x) {
        this.root.innerHTML = '';
        this.children = [];
        for (let i = 0; i < x.length; i++) {
            this.builderChild(x[i]);
        }
        this.builderChild(this.empty);
    }
    builderChild(value) {
        let ch = this.childBuilder();
        this.root.appendChild(ch.getElement());
        this.children.push(ch);
        ch.setParent(this);

        this.lastChild = ch;
        ch.value = value;

        ch.addChangeListener(() => {
            if (ch === this.lastChild) {
                if (!ch.isBlanc()) {
                    this.builderChild(this.empty);
                }
            } else {
                if (ch.isBlanc()) {
                    this.removeChild(ch);

                }
            }
        });
    }
    removeChild(ch) {
        ch.getElement().remove();
        let index = this.children.indexOf(ch);
        this.children.splice(index, 1);
    }
}

class ObjectFiled extends Field {

    constructor(conf, childBuilder, blankChecker) {
        super();
        this.fileds = {};
        this.blankChecker = blankChecker;

        this.element = childBuilder(this);
    }
    addField(name, field) {
        field.setParent(this);
        this.fileds[name] = field;
    }
    get value() {
        let ret = {};
        for (const [name, field] of Object.entries(this.fileds)) {
            ret[name] = field.value;
        }
        ;
        return ret;
    }
    set value(x) {
        for (const [name, field] of Object.entries(this.fileds)) {
            field.value = x[name];
        }
        ;
    }

    isBlanc() {
        return this.blankChecker(this.value);
    }
}

class HiddenFiled extends Field {
    constructor() {
        super();
        this.element = document.createElement('input');
        this.element.setAttribute('type', 'hidden');
    }

    get value() {
        return JSON.parse(this.element.value);
    }
    set value(x) {
        this.element.value = JSON.stringify(x);

    }
}