/* global doT */

class ListControl {
    constructor(formControl) {
        this.formControl = formControl;
        this.search = document.querySelector('#search');
        this.section = document.querySelector('section');
        this.addButton = document.querySelector('#add');
        this.plaeholder = this.section.innerHTML;
        this.lineTemplate = doT.template(document.querySelector('#dot-line').innerHTML);
        this.count = 0;

        this.search.form.addEventListener("submit", (e) => {
            e.preventDefault();
            return false;
        });
        this.search.addEventListener("input", () => this.startSearch());

        this.addButton.addEventListener("click", (e) => {
            this.click(null);
        });
        this.section.addEventListener("click", (e) => {
            let id = e.target.dataset.id;
            if (id) {
                this.click(id);
            }
        });
        

        this.startSearch();
        window.addEventListener('scroll', (e) => this.scroll());
    }
    startSearch() {
        this.offset = 0;
        this.count++;
        this.query = this.search.value;
        this.section.innerHTML = this.plaeholder;
        this.section.style['min-height'] = '0';
        this.pageProcess = false;
        this.lastPage = false;
        this.getPage();
    }
    async getPage() {
        if (this.pageProcess || this.lastPage) {
            return;
        }
        this.pageProcess = true;
        const count = this.count;
        do {
            let response = await fetch(searchUrlBulder(this.offset, this.query)),
                    page = await response.json();
            if (count !== this.count) {
                return;
            }
            this.offset++;
            if (page.first) {
                this.section.innerHTML = '';
                this.section.style['min-height'] = (page.totalElements) * 1.51 + 'em';
            }
            if (page.last) {
                this.lastPage = true;
            }
            this.renderPage(page);
        } while (this.isOnBottom() && !this.lastPage);
        this.pageProcess = false;
    }
    renderPage(page) {
        let content = this.lineTemplate(page);
        this.section.innerHTML += content;

    }
    click(id) {
        this.formControl.open(id);
    }
    isOnBottom() {
        return elementAboveBottomViewport(this.section.lastElementChild);
    }
    scroll() {
        if (this.isOnBottom()) {
            this.getPage();
        }
    }
}