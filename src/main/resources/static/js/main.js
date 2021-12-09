
$(document).ready(function(){
    $('.editBtn').on('click', function (event) {
        event.preventDefault();
        let href = $(this).attr('href');
        $.get(href, function (user, status){
            $('#editId').val(user.id);
            $('#editActive').val(user.active);
            $('#editName').val(user.name);
            $('#editLastname').val(user.lastname);
            $('#editAge').val(user.age);
            $('#editEmail').val(user.email);
            $('#editPassword').val(user.password);
            $('#editRoles').val(user.roles);
        });
        $('#editModal').modal('show');
    });

    $('.deleteBtn').on('click', function (event) {
        event.preventDefault();
        let href = $(this).attr('href');
        $.get(href, function (user, status){
            $('#deleteId').val(user.id);
            $('#deleteActive').val(user.active);
            $('#deleteName').val(user.name);
            $('#deleteLastname').val(user.lastname);
            $('#deleteAge').val(user.age);
            $('#deleteEmail').val(user.email);
            $('#deletePassword').val(user.password);
            $('#deleteRoles').val(user.roles);
        });
        $('#deleteModal').modal('show');
    });
});
