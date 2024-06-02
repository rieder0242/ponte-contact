/* global doT */

class FormControl {
    constructor() {
        this.modal = new Modal();

        this.form = html2Element(document.querySelector('#dot-edit-form').innerHTML);
        this.modal.appendChild(this.form);

        let [adrsColumn, telsColumn, mailsColumn] = this.form.querySelectorAll('.col');

        this.name = new InputField({name: "Name"}).addValidator(new NotBlancValidator());
        insertBefore(adrsColumn, this.name.element);

        this.birthDate = new InputField({name: "Birth date", type: 'date'});
        insertBefore(adrsColumn, this.birthDate.element);

        this.motherName = new InputField({name: "Name of mother"});
        insertBefore(adrsColumn, this.motherName.element);

        this.socialSecurityNumber = new InputField({name: "Social security number"});
        insertBefore(adrsColumn, this.socialSecurityNumber.element);

        this.taxNumber = new InputField({name: "Tax number"});
        insertBefore(adrsColumn, this.taxNumber.element);

        this.addresses = new ArrayFiled({}, adrsColumn, () => {
            return new ObjectFiled({}, (parent) => {
                let e = html2Element(doT.template(document.querySelector('#dot-address').innerHTML)()),
                        lines = e.querySelectorAll('.line');

                let id = new HiddenFiled();
                parent.addField('id', id);
                lines[0].appendChild(id.element)

                let zipcode = new InputField({name: 'Zipcode', placeholder: '1234', clazz: 'zipcode'});
                parent.addField('zipcode', zipcode);
                lines[0].appendChild(zipcode.element)

                let settlement = new InputField({name: 'Settlement', clazz: 'settlement'});
                parent.addField('settlement', settlement);
                lines[0].appendChild(settlement.element)

                let street = new InputField({name: 'Street', clazz: 'street'});
                parent.addField('street', street);
                lines[1].appendChild(street.element)

                let houseNumber = new InputField({name: 'House Number', clazz: 'houseNumber'});
                parent.addField('houseNumber', houseNumber);
                lines[1].appendChild(houseNumber.element)

                return e;
            },
                    (v) => {
                return v.zipcode == '' && v.settlement == '' && v.street == '' && v.houseNumber == '';
            }
            );
        }, {'id': null, 'zipcode': '', 'settlement': '', 'street': '', 'houseNumber': ''
        });
        this.tels = new ArrayFiled({}, telsColumn, () => {
            return new InputField({name: 'Phone', placeholder: 'Add phone number'}).addValidator(new PhoneValidator());
        }, "");
        this.emails = new ArrayFiled({}, mailsColumn, () => {
            return new InputField({name: 'E-mail', placeholder: 'Add e-mail address'}).addValidator(new EmailValidator());
        }, "");


        this.form.querySelector('.quit').addEventListener("click", (e) => this.close());

        this.modal.onclick(() => this.close());
        this.form.addEventListener("submit", (e) => {
            e.preventDefault();
            setTimeout(() => this.save());
            return false;
        });

        this.messageBox = new MessageBox("Do you really delete it?").addButton('yes', 'yes').addButton('no', 'no');
        let del = this.form.querySelector("#delete");
        del.addEventListener("click", (e) => {
            e.preventDefault();
            e.stopPropagation();
            this.deleteClick();
            return false;
        });
    }

    async deleteClick() {
        if (this.id === null) {
            new Notify('New contact is not deletable.');
            return;
        }
        if (await this.messageBox.show() === 'yes') {
            this.close();

            let headers = {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
                    token = document.querySelector("meta[name='_csrf']").getAttribute("content"),
                    headerName = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

            headers[headerName] = token;

            let response = await fetch(contactUrlBulderById(this.id), {
                headers: headers,
                method: 'DELETE'
            });
            new Notify('Contact is deleted.');
            this.listControl.startSearch();
        }
        return false;

    }
    async open(id) {
        this.id = id;
        this.modal.show();

        this.name.value = "";
        this.birthDate.value = "";
        this.motherName.value = "";
        this.socialSecurityNumber.value = "";
        this.taxNumber.value = "";
        this.addresses.placeholderOn();
        this.tels.placeholderOn();
        this.emails.placeholderOn();

        let contact;
        if (id === null) {
            contact = this.createEmptyContact();
        } else {
            contact = await this.fetchContact(id);

        }
        this.name.value = contact.name;
        this.birthDate.value = contact.birthDate;
        this.motherName.value = contact.motherName;
        this.socialSecurityNumber.value = contact.socialSecurityNumber;
        this.taxNumber.value = contact.taxNumber;
        this.addresses.value = contact.addresses;
        this.tels.value = contact.tels;
        this.emails.value = contact.emails;
    }

    createEmptyContact() {
        return {
            name: '',
            birthDate: null,
            motherName: '',
            socialSecurityNumber: '',
            taxNumber: '',
            addresses: [],
            tels: [],
            emails: []
        };
    }
    async fetchContact(id) {
        let response = await fetch(contactUrlBulderById(id)),
                contact = await response.json();
        return contact;

    }

    async save() {
        let hasError = this.form.querySelectorAll('.hasError').length > 0;
        if (hasError) {
            new Notify('Invalid data!', 'error');
        } else {
            let contact = {
                id: this.id,
                name: this.name.value,
                birthDate: this.birthDate.value,
                motherName: this.motherName.value,
                socialSecurityNumber: this.socialSecurityNumber.value,
                taxNumber: this.taxNumber.value,
                tels: this.tels.value,
                emails: this.emails.value,
                addresses: this.addresses.value
            };
            this.close();
            let headers = {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
                    token = document.querySelector("meta[name='_csrf']").getAttribute("content"),
                    headerName = document.querySelector("meta[name='_csrf_header']").getAttribute("content");
            headers[headerName] = token;

            let url, method;
            if (this.id === null) {
                url = contactUrlBulder();
                method = 'POST';
            } else {
                url = contactUrlBulderById(this.id);
                method = 'PUT';
            }

            let response = await fetch(url, {
                body: JSON.stringify(contact),
                headers: headers,
                method: method
            });

            contact = await response.json();
            
            new Notify('Contact is saved.');
            this.listControl.startSearch();
        }
    }

    close() {
        this.modal.hide();
    }
    setListControl(listControl) {
        this.listControl = listControl;
    }
}
