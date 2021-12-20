$(async function () {
    await getTableWithUsers();
    await getDefaultModal();
    await addNewUser();
})


const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },
    findAllUsers: async () => await fetch('/admin/allUsers'),
    findOneUser: async (id) => await fetch(`admin/${id}`),
    addNewUser: async (user) => await fetch('admin/saveUser', {
        method: 'POST',
        headers: userFetchService.head,
        body: JSON.stringify(user)
    }),
    updateUser: async (user, id) => await fetch(`admin/${id}`, {
        method: 'PATCH',
        headers: userFetchService.head,
        body: JSON.stringify(user)
    }),
    deleteUser: async (id) => await fetch(`admin/${id}`, {
        method: 'DELETE',
        headers: userFetchService.head
    })
}

async function getTableWithUsers() {
    let table = $('#usersTable tbody');
    table.empty();

    await userFetchService.findAllUsers()
        .then(res => res.json())
        .then(users => {
            users.forEach(user => {
                let tableFilling = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.name}</td>
                            <td>${user.lastname}</td>
                            <td>${user.email}</td>
                            <td>${user.age}</td>
                            <td>${user.roles.map(roleMap => roleMap.role.slice(5))}</td>           
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="edit" class="btn btn-primary" 
                                data-toggle="modal" data-bs-target="#someDefaultModal">Edit</button>
                            </td>
                            <td>
                                <button type="button" data-userid="${user.id}" data-action="delete" class="btn btn-danger" 
                                data-toggle="modal" data-bs-target="#someDefaultModal">Delete</button>
                            </td>
                        </tr>
                )`;
                table.append(tableFilling);
            })
        })

    $("#usersTable").find('button').on('click', (event) => {
        let defaultModal = $('#someDefaultModal');
        let targetButton = $(event.target);
        let buttonUserId = targetButton.attr('data-userid');
        let buttonAction = targetButton.attr('data-action');

        defaultModal.attr('data-userid', buttonUserId);
        defaultModal.attr('data-action', buttonAction);
        defaultModal.modal('show');
    })
}

async function getDefaultModal() {
    $('#someDefaultModal').modal({
        keyboard: true,
        backdrop: "static",
        show: false
    }).on("show.bs.modal", (event) => {
        let thisModal = $(event.target);
        let userid = thisModal.attr('data-userid');
        let action = thisModal.attr('data-action');
        switch (action) {
            case 'edit':
                editUser(thisModal, userid);
                break;
            case 'delete':
                deleteUser(thisModal, userid);
                break;
        }
    }).on("hidden.bs.modal", (e) => {
        let thisModal = $(e.target);
        thisModal.find('.modal-title').html('');
        thisModal.find('.modal-body').html('');
        thisModal.find('.modal-footer').html('');
    })
}

async function editUser(modal, id) {
    let preuser = await userFetchService.findOneUser(id);
    let user = preuser.json();

    modal.find('.modal-title').html('Edit user');

    let editButton = `<button type="submit" class="btn btn-primary" id="editButton">Edit</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(editButton);
    modal.find('.modal-footer').append(closeButton);

    user.then(user => {
        let bodyForm = `
            <form class="form-group" id="editUser">
                
                <input type="hidden" value="${user.id}" name="editId" id="editId" />
                <input type="hidden" value="${user.active}" name="editActive" id="editActive"/>
                <label for="editName" class="col-form-label fw-bold">FirstName: </label>
                <input type="text" value="${user.name}" id="editName" name="editName"
                       class="form-control"
                       required minlength="2" maxlength="20"/>
                <label for="editLastname" class="col-4 col-form-label fw-bold">LastName: </label>
                <input type="text" value="${user.lastname}" id="editLastname" name="editLastname"
                       class="form-control"
                       required minlength="2" maxlength="20"/>
                <label for="editAge" class="col-4 col-form-label fw-bold">Age: </label>
                <input type="number" value="${user.age}" id="editAge" name="editAge"
                       class="form-control"
                       required minlength="1" maxlength="3"/>
                <label for="editEmail" class="col-4 col-form-label fw-bold">Email: </label>
                <input type="text" value="${user.email}" id="editEmail" name="editEmail"
                       class="form-control"
                       required minlength="1" maxlength="40"/>
                <label for="editPassword" class="col-4 col-form-label fw-bold">Password: </label>
                <input type="password" value="${user.password}" id="editPassword" name="editPassword"
                       class="form-control"
                       required minlength="6" maxlength="12"/>
                <label for="editRoles" class="col-4 col-form-label fw-bold">Roles: </label>
                <select multiple size="2" class="form-control" id="editRoles" name="editRoles">
                        <option value="ROLE_ADMIN" >ADMIN</option>
                        <option value="ROLE_USER">USER</option>
                </select>
                        
            </form>
        `;
        modal.find('.modal-body').append(bodyForm);
    })

    $("#editButton").on('click', async () => {
        let id = modal.find("#editId").val().trim();
        let active = modal.find("#editActive").val().trim();
        let name = modal.find("#editName").val().trim();
        let lastname = modal.find("#editLastname").val().trim();
        let age = modal.find("#editAge").val().trim();
        let email = modal.find("#editEmail").val().trim();
        let password = modal.find("#editPassword").val().trim();
        let roles = modal.find("#editRoles").val();

        let data = {
            id: id,
            active: active,
            name: name,
            lastname: lastname,
            age: age,
            email: email,
            password: password,
            roles: roles
        }
        const response = await userFetchService.updateUser(data, id);

        if (response.ok) {
            getTableWithUsers();
            modal.modal('hide');
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            modal.find('.modal-body').prepend(alert);
        }
    })
}


async function deleteUser(modal, id) {
    let preuser = await userFetchService.findOneUser(id);
    let user = preuser.json();
    modal.find('.modal-title').html('Delete User');

    let deleteButton = `<button type="submit" class="btn btn-danger" id="deleteButton">Delete</button>`;
    let closeButton = `<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>`
    modal.find('.modal-footer').append(deleteButton);
    modal.find('.modal-footer').append(closeButton);

    user.then(user => {
        let bodyForm = `
            <form class="form-group" id="deleteUser">
                
                <input type="hidden" value="${user.id}" name="deleteId" id="deleteId" />
                <input type="hidden" value="${user.active}" name="deleteActive" id="deleteActive"/>
                <label for="deleteName" class="col-form-label fw-bold">FirstName: </label>
                <input type="text" value="${user.name}" id="deleteName" name="deleteName"
                       class="form-control" disabled/>
                <label for="deleteLastname" class="col-4 col-form-label fw-bold">LastName: </label>
                <input type="text" value="${user.lastname}" id="deleteLastname" name="deleteLastname"
                       class="form-control" disabled/>
                <label for="deleteAge" class="col-4 col-form-label fw-bold">Age: </label>
                <input type="number" value="${user.age}" id="deleteAge" name="deleteAge"
                       class="form-control" disabled/>
                <label for="deleteEmail" class="col-4 col-form-label fw-bold">Email: </label>
                <input type="text" value="${user.email}" id="deleteEmail" name="deleteEmail"
                       class="form-control" disabled/>
                <label for="deletePassword" class="col-4 col-form-label fw-bold">Password: </label>
                <input type="password" value="${user.password}" id="deletePassword" name="deletePassword"
                       class="form-control" disabled/>
                <label for="deleteRoles" class="col-4 col-form-label fw-bold">Roles: </label>    
                <select multiple size="2" class="form-control" id="deleteRoles" name="deleteRoles" disabled>
                        <option value="ROLE_ADMIN">ADMIN</option>
                        <option value="ROLE_USER">USER</option>
                </select>                                          
            </form>
        `;
        modal.find('.modal-body').append(bodyForm);
    })

    $("#deleteButton").on('click', async () => {

        const response = await userFetchService.deleteUser(id);

        if (response.ok) {
            await getTableWithUsers();
            modal.modal('hide');
        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            modal.find('.modal-body').prepend(alert);
        }
    })
}

async function addNewUser() {
    $('#addUserButton').on('click', async () => {
        let addUserForm = $('#defaultSomeForm')
        let id = addUserForm.find('#addId').val();
        let active = addUserForm.find('#addActive').val();
        let name = addUserForm.find('#addName').val();
        let lastname = addUserForm.find('#addLastName').val();
        let age = addUserForm.find('#addAge').val();
        let email = addUserForm.find('#addEmail').val();
        let password = addUserForm.find('#addPassword').val();
        let roles = addUserForm.find("#addRoles").val();

        let data = {
            id: id,
            active: active,
            name: name,
            lastname: lastname,
            age: age,
            password: password,
            email: email,
            roles: roles
        }

        const response = await userFetchService.addNewUser(data);
        if (response.ok) {
            await getTableWithUsers();

        } else {
            let body = await response.json();
            let alert = `<div class="alert alert-danger alert-dismissible fade show col-12" role="alert" id="sharaBaraMessageError">
                            ${body.info}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>`;
            addUserForm.prepend(alert)
        }
    })
}



