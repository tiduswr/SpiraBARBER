var myModal;
var id;
var selectedRow;
const table = document.getElementById("servico-list");
const imageModalContent = document.getElementById('imageModalContent');

//Document Ready
document.addEventListener("DOMContentLoaded", () => {
    new Cleave('#preco', {
        numeral: true,
        numeralDecimalMark: ',',
        delimiter: '.'
    });
    myModal = new bootstrap.Modal(document.getElementById('confirmationModal'));
    imageModal = new bootstrap.Modal(document.getElementById('imageModal'));
});

function configureImageModal(target){
    selectedRow = target.parentElement.parentElement;
    imageLink = selectedRow.children[1].textContent;
    imageModalContent.innerHTML = `<img style="display: block; max-width: 100%; height: auto;" src="/api/servicos/image/${imageLink}" class="img-responsive">`;
    imageModal.show();
}

//Pega o id do item ao clicar no botão delete
table.onclick = ({target}) =>{
    if(target.classList.contains("delete")){
        configureDeleteConfirmationModal(target);
    }else if(target.parentElement.classList.contains("delete")){
        configureDeleteConfirmationModal(target.parentElement)
    }else if(target.classList.contains("image")){
        configureImageModal(target);
    }else if(target.parentElement.classList.contains("image")){
        configureImageModal(target.parentElement);
    }
};
document.querySelector(".modal-confirm-deletion").addEventListener("click", (e) => {
    deleteItem(id);
});
function configureDeleteConfirmationModal(target){
    selectedRow = target.parentElement.parentElement;
    id = selectedRow.children[0].textContent;
    prepareConfirmationModal("Confirme a operação", "Você deseja realmente excluir esse registro?");
    myModal.show();
}

//Show allerts
function showAlert(message, className){
    const div = document.createElement("div");
    div.className = `alert alert-${className}`;

    div.appendChild(document.createTextNode(message));
    const container = document.querySelector(".container");
    const main = document.querySelector(".main");
    container.insertBefore(div, main);
    div.focus();

    setTimeout(() => document.querySelector(".alert").remove(), 3000);
}

//Configura o alerta nos botões delete
function prepareConfirmationModal(title, message){
    document.getElementById("modal-title").innerHTML = title;
    document.getElementById("modal-message").innerHTML = message;
}
function deleteItem(id){
    var url = "/users/servicos/delete/" + id;
    window.location.href = url;
}