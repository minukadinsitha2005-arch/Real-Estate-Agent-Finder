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
  window.location.href = "auth?mode=" + type;
}

function closeModal() {
  if (authModal) {
    authModal.classList.remove("show");
  }
}

if (authModal) {
  authModal.addEventListener("click", function (e) {
    if (e.target === authModal) {
      closeModal();
    }
  });
}

function goToProperties() {
  window.location.href = "properties.html";
}


window.addEventListener("DOMContentLoaded", function () {
  const params = new URLSearchParams(window.location.search);

  if (params.get("submitted") === "1") {
    alert("Your request was submitted successfully.");
  }

  if (params.get("contactSubmitted") === "1") {
    alert("Your message was sent successfully.");
  }

  if (params.get("auth") === "registered") {
    alert("Registration successful. Please log in.");
  }

  if (document.getElementById("agentsListGrid")) {
    searchAgents();
  }

  if (document.getElementById("propertiesGrid")) {
    searchProperties();
  }
});


async function searchAgents() {
  const input = document.getElementById("agentSearchInput");
  const query = input ? input.value.trim() : "";
  const grid = document.getElementById("agentsListGrid");
  if (!grid) {
    return;
  }

  try {
    const response = await fetch(`api/agents?q=${encodeURIComponent(query)}`);
    const agents = await response.json();
    renderAgents(agents, grid);
  } catch (error) {
    console.error("Failed to load agents", error);
  }
}

function renderAgents(agents, grid) {
  if (!Array.isArray(agents) || agents.length === 0) {
    grid.innerHTML = '<div class="agent-profile-card"><div class="agent-profile-content"><h3>No agents found</h3><p>Try a different search keyword or city.</p></div></div>';
    return;
  }

  grid.innerHTML = agents.map((agent) => `
    <div class="agent-profile-card reveal active">
      <img src="images/agent1.jpg" alt="${agent.fullName}">
      <div class="agent-profile-content">
        <span class="agent-badge">${agent.status || 'Available'}</span>
        <h3>${agent.fullName}</h3>
        <p><strong>Location:</strong> ${agent.city || ''}</p>
        <p><strong>Specialty:</strong> ${agent.specialty || ''}</p>
        <p><strong>Experience:</strong> ${agent.experienceYears || 0} Years</p>
        <p class="agent-description">${agent.bio || ''}</p>
        <div class="agent-stars">★★★★★</div>
        <a href="appointments.html" class="agent-action-btn">Book Appointment</a>
      </div>
    </div>
  `).join("");
}

async function searchProperties() {
  const input = document.getElementById("propertySearchInput");
  const query = input ? input.value.trim() : "";
  const grid = document.getElementById("propertiesGrid");
  if (!grid) {
    return;
  }

  try {
    const response = await fetch(`api/properties?q=${encodeURIComponent(query)}`);
    const properties = await response.json();
    renderProperties(properties, grid);
  } catch (error) {
    console.error("Failed to load properties", error);
  }
}

function renderProperties(properties, grid) {
  if (!Array.isArray(properties) || properties.length === 0) {
    grid.innerHTML = '<div class="featured-property-card"><div class="featured-property-content"><h3>No properties found</h3><p>Try another keyword or city.</p></div></div>';
    return;
  }

  grid.innerHTML = properties.map((property) => `
    <div class="featured-property-card reveal active">
      <img src="${property.imageUrl || 'images/property1.jpg'}" alt="${property.title}">
      <div class="featured-property-content">
        <h3>${property.title}</h3>
        <div class="featured-price">Rs. ${Number(property.price || 0).toLocaleString()}</div>
        <p>${property.bedrooms || 0} Beds | ${property.bathrooms || 0} Baths</p>
        <p>${property.city || ''}</p>
        <a href="appointments.html" class="featured-property-btn">Book Visit</a>
      </div>
    </div>
  `).join("");
}
