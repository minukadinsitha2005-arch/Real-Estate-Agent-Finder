function searchHome() {
  let input = document.querySelector("input").value;

  if (input === "") {
    alert("Please describe the home you want!");
  } else {
    alert("Searching for: " + input);
  }
}

function revealOnScroll() {
  const reveals = document.querySelectorAll(".reveal");

  reveals.forEach((element) => {
    const windowHeight = window.innerHeight;
    const elementTop = element.getBoundingClientRect().top;
    const visiblePoint = 120;

    if (elementTop < windowHeight - visiblePoint) {
      element.classList.add("active");
    }
  });
}

window.addEventListener("scroll", revealOnScroll);
window.addEventListener("load", revealOnScroll);

function scrollLocations(direction) {
  const container = document.getElementById("locationsScroll");
  const scrollAmount = 320;

  container.scrollBy({
    left: direction * scrollAmount,
    behavior: "smooth"
  });
}

function scrollAgents(direction) {
  const container = document.getElementById("agentsScroll");
  const scrollAmount = 340;

  container.scrollBy({
    left: direction * scrollAmount,
    behavior: "smooth"
  });
}

function scrollAvailableAgents(direction) {
  const container = document.getElementById("availableAgentsScroll");
  const scrollAmount = 340;

  container.scrollBy({
    left: direction * scrollAmount,
    behavior: "smooth"
  });
}

const authModal = document.getElementById("authModal");
const authTitle = document.getElementById("authTitle");
const continueBtn = document.getElementById("continueBtn");

function openModal(type) {
  authModal.classList.add("show");

  if (type === "login") {
    authTitle.textContent = "Log in to your account";
    continueBtn.textContent = "Log In";
  } else {
    authTitle.textContent = "Create your account";
    continueBtn.textContent = "Sign Up";
  }
}

function closeModal() {
  authModal.classList.remove("show");
}

authModal.addEventListener("click", function (e) {
  if (e.target === authModal) {
    closeModal();
  }
});

function goToProperties() {
  window.location.href = "properties.html";
}

