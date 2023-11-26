const urlUsers = '/admin/users/';
const urlRoles = '/admin/roles/';

const allUsers = fetch(urlUsers).then(response => response.json())
const allRoles = fetch(urlRoles).then(response => response.json())

const deleteUserModal = new bootstrap.Modal(document.getElementById('deleteUserModal'))
const editUserModal = new bootstrap.Modal(document.getElementById('editUserModal'))

const on = (element, event, selector, handler) => {
    element.addEventListener(event, e => {
        if (e.target.closest(selector)) {
            handler(e.target)
        }
    })
}

// Заполнение таблицы пользователей
allUsers.then(users => {
        let result = ''
        for (const i in users) {
            let roles = ''
            users[i].roles.forEach(role => {
                roles += ' '
                roles += role.name.replace("ROLE_", "")
            })
            result += `<tr>
                    <td>${users[i].id}</td>
                    <td>${users[i].firstName}</td>
                    <td>${users[i].lastName}</td>
                    <td>${users[i].age}</td>
                    <td>${users[i].email}</td>
                    <td>${roles}</td>
                    <td>
                        <button type="button" class="btn btn-info btn-sm text-white" id="editUserBtn"}">Edit</button>
                    </td>
                    <td>
                        <button type="button" class="btn btn-danger btn-sm" id="deleteUserBtn">Delete</button>
                    </td>
                    </tr>`
        }
        document.getElementById("users-info-table").innerHTML = result
    }
)

// Функция заполнения ролей
fillRoles = function (elementId) {
    allRoles.then(roles => {
        let result = ''
        for (const i in roles) {
            result += `<option value = ${roles[i].id}>${roles[i].name.replace("ROLE_", "")}</option>`
        }
        document.getElementById(elementId).innerHTML = result
    })
}

fillRoles("role_select")


//Добавление нового пользователя
document.getElementById('newUserForm').addEventListener('submit', (e) => {
    e.preventDefault()
    let role = document.getElementById('role_select')
    let rolesAddUser = []
    let rolesAddUserValue = ''
    for (let i = 0; i < role.options.length; i++) {
        if (role.options[i].selected) {
            rolesAddUser.push({id: role.options[i].value, name: 'ROLE_' + role.options[i].innerHTML})
            rolesAddUserValue += role.options[i].innerHTML + ' '
        }
    }
    fetch(urlUsers, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        },
        body: JSON.stringify({
            firstName: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            age: document.getElementById('age').value,
            email: document.getElementById('email').value,
            password: document.getElementById('password').value,
            roles: rolesAddUser
        })
    })
        .then(response => {
            if (!response.ok) {
                response.json().then(data => {
                    let result = ''
                    data.forEach(error => {
                        result += error.fieldName + ': ' + error.message + '\n'
                    })
                    alert(result)
                })
                throw new Error('Network response was not ok')
            }
            return response.json()
        })
        .then(user => {
            let newRow = document.createElement('tr')
            newRow.innerHTML = `<tr>
                           <td>${user.id}</td>
                           <td>${user.firstName}</td>
                           <td>${user.lastName}</td>
                           <td>${user.age}</td>
                           <td>${user.email}</td>
                           <td>${rolesAddUserValue}</td>
                           <td><button type="button" class="btn btn-info btn-sm text-white" id="editUserBtn">Edit</button></td>
                           <td><button type="button" class="btn btn-danger btn-sm" id="deleteUserBtn">Delete</button></td>
                           </tr>`
            document.getElementById("users-info-table").appendChild(newRow)
            document.getElementById('newUserForm').reset()
            document.getElementById("all-users-tab").click()
        })
        .catch((error) => {
            console.log(error);
        })
})

// Изменение пользователя
const idEdit = document.getElementById('id_edit')
const firstNameEdit = document.getElementById('firstName_edit')
const lastNameEdit = document.getElementById('lastName_edit')
const ageEdit = document.getElementById('age_edit')
const emailEdit = document.getElementById('email_edit')
const passwordEdit = document.getElementById('password_edit')
const rolesEdit = document.getElementById('role_edit')

let rowEdit = null

on(document, 'click', '#editUserBtn', e => {
    rowEdit = e.parentNode.parentNode

    idEdit.value = rowEdit.children[0].innerHTML
    firstNameEdit.value = rowEdit.children[1].innerHTML
    lastNameEdit.value = rowEdit.children[2].innerHTML
    ageEdit.value = rowEdit.children[3].innerHTML
    emailEdit.value = rowEdit.children[4].innerHTML
    passwordEdit.value = ''
    let option = ''
    allRoles.then(roles => {
        roles.forEach(role => {
            let selected = rowEdit.children[5].innerHTML.includes(role.name.replace('ROLE_', '')) ? 'selected' : ''
            option += `<option value="${role.id}" ${selected}>${role.name.replace('ROLE_', '')}</option>`
        })
        rolesEdit.innerHTML = option
    })
    editUserModal.show()
})

document.getElementById('edit_user_form').addEventListener('submit', (e) => {
    e.preventDefault()
    let role = document.getElementById('role_edit')
    let rolesUserEdit = []
    let rolesUserEditValue = ''
    for (let i = 0; i < role.options.length; i++) {
        if (role.options[i].selected) {
            rolesUserEdit.push({id: role.options[i].value, name: 'ROLE_' + role.options[i].innerHTML})
            rolesUserEditValue += role.options[i].innerHTML + ' '
        }
    }
    fetch(urlUsers + 'updateUser', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id: idEdit.value,
            firstName: firstNameEdit.value,
            lastName: lastNameEdit.value,
            age: ageEdit.value,
            email: emailEdit.value,
            password: passwordEdit.value,
            roles: rolesUserEdit
        })
    })
        .then(response => {
            if (!response.ok) {
                response.json().then(data => {
                    let result = ''
                    data.forEach(error => {
                        result += error.fieldName + ': ' + error.message + '\n'
                    })
                    alert(result)
                })
                throw new Error('Network response was not ok')
            }
            return response.json();
        }).then(user => {
            rowEdit.children[0].innerHTML = idEdit.value
            rowEdit.children[1].innerHTML = firstNameEdit.value
            rowEdit.children[2].innerHTML = lastNameEdit.value
            rowEdit.children[3].innerHTML = ageEdit.value
            rowEdit.children[4].innerHTML = emailEdit.value
            rowEdit.children[5].innerHTML = rolesUserEditValue
            editUserModal.hide()
        }
    )
        .catch(error => console.log(error))

})

// Удаление пользователя
let rowDelete = null
on(document, 'click', '#deleteUserBtn', e => {
    rowDelete = e.parentNode.parentNode
    document.getElementById('id_delete').value = rowDelete.children[0].innerHTML
    document.getElementById('firstName_delete').value = rowDelete.children[1].innerHTML
    document.getElementById('lastName_delete').value = rowDelete.children[2].innerHTML
    document.getElementById('age_delete').value = rowDelete.children[3].innerHTML
    document.getElementById('email_delete').value = rowDelete.children[4].innerHTML

    let option = ''
    allRoles.then(roles => {
        roles.forEach(role => {
            if (rowDelete.children[5].innerHTML.includes(role.name.replace('ROLE_', ''))) {
                option += `<option value="${role.id}">${role.name.replace('ROLE_', '')}</option>`
            }
        })
        document.getElementById('role_delete').innerHTML = option
    })
    deleteUserModal.show()
})

document.getElementById('delete_user_form').addEventListener('submit', (e) => {
    e.preventDefault()
    fetch(urlUsers + 'deleteUser/?id=' + rowDelete.children[0].innerHTML, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8'
        }
    }).then(() => {
        deleteUserModal.hide()
        rowDelete.parentNode.removeChild(rowDelete)
    })
})