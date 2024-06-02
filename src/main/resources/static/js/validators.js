class NotBlancValidator {
    valid(v) {
        if (v == '') {
            return [false, "Field is requier!"];
        } else {
            return [true];
        }
    }
}
class EmailValidator {
    constructor() {
        this.re = /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/;
    }
    valid(v) {
        if (this.re.test(v)) {
            return [true];
        } else {
            return [false, "Wrong email format!"];
        }
    }
}

class PhoneValidator {
    constructor() {
        this.re = /[^0-9-+() ]/;
    }
    valid(v) {
        let match = v.match(this.re);
        if (match) {
            return [false, 'Wrong charater "' + match[0] + '"'];
        } else {
            return [true];
        }
    }
}