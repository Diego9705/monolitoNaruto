function configurarModal(modalId, btnId, closeIndex) {
  var modal = document.getElementById(modalId);
  var btn = document.getElementById(btnId);
  var span = document.getElementsByClassName("close")[closeIndex];

  btn.onclick = () => modal.style.display = "block";
  span.onclick = () => modal.style.display = "none";

  window.addEventListener("click", (event) => {
    if (event.target === modal) {
      modal.style.display = "none";
    }
  });
}

configurarModal("myModalAldea", "myBtnAldea", 0);
configurarModal("myModalNinja", "myBtnNinja", 1);
configurarModal("myModalMision", "myBtnMision", 2);
configurarModal("myModalJutsu", "myBtnJutsu", 3);
