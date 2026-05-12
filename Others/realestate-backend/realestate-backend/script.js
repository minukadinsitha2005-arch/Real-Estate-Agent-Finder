// -------------------------------------------------------
// RealtorSL - Frontend API Connection Script
// Connects to Spring Boot backend at localhost:8080
// -------------------------------------------------------

const API_BASE_URL = "http://localhost:8080/api";

// ======================================================
// SCROLL REVEAL ANIMATION
// ======================================================
function revealOnScroll() {
  const elements = document.querySelectorAll(".reveal");
  const windowHeight = window.innerHeight;

  elements.forEach((el) => {
    const rect = el.getBoundingClientRect();
    if (rect.top < windowHeight - 80) {
      el.classList.add("visible");
    }
  });
}

window.addEventListener("scroll", revealOnScroll);
window.addEventListener("load", revealOnScroll);

// ======================================================
// SEARCH HOME (index.html)
// ======================================================
function searchHome() {
  const query = document.querySelector(".search-box input")?.value?.trim();
  if (query) {
    window.location.href = `properties.html?keyword=${encodeURIComponent(query)}`;
  }
}

function goToProperties() {
  window.location.href = "properties.html";
}

// ======================================================
// AUTH MODAL (index.html)
// ======================================================
function openModal(type) {
  const modal = document.getElementById("authModal");
  if (modal) modal.classList.add("show");
}

function closeModal() {
  const modal = document.getElementById("authModal");
  if (modal) modal.classList.remove("show");
}

// ======================================================
// LOCATIONS SLIDER (properties.html)
// ======================================================
function scrollLocations(direction) {
  const slider = document.getElementById("locationsScroll");
  if (slider) {
    slider.scrollBy({ left: direction * 300, behavior: "smooth" });
  }
}

// ======================================================
// AGENTS PAGE - Load and display agents from backend
// ======================================================
async function loadAgents(searchParams = {}) {
  const grid = document.querySelector(".agents-list-grid");
  if (!grid) return; // not on agents page

  grid.innerHTML = "<p style='color:#888; padding:20px;'>Loading agents...</p>";

  try {
    let url = `${API_BASE_URL}/agents`;
    const params = new URLSearchParams(searchParams);
    if ([...params].length > 0) url += `?${params.toString()}`;

    const response = await fetch(url);
    const agents = await response.json();

    if (agents.length === 0) {
      grid.innerHTML = "<p style='color:#888; padding:20px;'>No agents found.</p>";
      return;
    }

    grid.innerHTML = agents.map(agent => `
      <div class="agent-profile-card reveal">
        <img src="${agent.imageUrl || 'images/agent1.jpg'}" alt="${agent.name}" 
             onerror="this.src='images/agent1.jpg'" />
        <div class="agent-profile-content">
          <span class="agent-badge">${agent.badge || 'Verified'}</span>
          <h3>${agent.name}</h3>
          <p><strong>Location:</strong> ${agent.location}</p>
          <p><strong>Specialty:</strong> ${agent.specialization}</p>
          <p><strong>Experience:</strong> ${agent.experienceYears} Years</p>
          <p class="agent-description">${agent.description || ''}</p>
          <div class="agent-stars">${'★'.repeat(Math.round(agent.rating || 5))}</div>
          <a href="appointments.html?agent=${encodeURIComponent(agent.name)}" class="agent-action-btn">
            Book Appointment
          </a>
        </div>
      </div>
    `).join('');

    revealOnScroll();
  } catch (error) {
    console.error("Error loading agents:", error);
    grid.innerHTML = "<p style='color:red; padding:20px;'>Failed to load agents. Make sure the backend is running.</p>";
  }
}

// Agents search button
function searchAgents() {
  const input = document.querySelector(".hero .search-box input");
  const query = input?.value?.trim();
  if (query) {
    // Try location first, then specialization
    loadAgents({ location: query });
  } else {
    loadAgents();
  }
}

// ======================================================
// PROPERTIES PAGE - Load and display properties
// ======================================================
async function loadProperties(searchParams = {}) {
  const grid = document.querySelector(".featured-properties-grid");
  if (!grid) return; // not on properties page

  grid.innerHTML = "<p style='color:#888; padding:20px;'>Loading properties...</p>";

  try {
    let url = `${API_BASE_URL}/properties`;
    const params = new URLSearchParams(searchParams);
    if ([...params].length > 0) url += `?${params.toString()}`;

    const response = await fetch(url);
    const properties = await response.json();

    if (properties.length === 0) {
      grid.innerHTML = "<p style='color:#888; padding:20px;'>No properties found.</p>";
      return;
    }

    grid.innerHTML = properties.map(prop => `
      <div class="featured-property-card reveal">
        <img src="${prop.imageUrl || 'images/property1.jpg'}" alt="${prop.title}"
             onerror="this.src='images/property1.jpg'" />
        <div class="featured-property-content">
          <h3>${prop.title}</h3>
          <div class="featured-price">Rs. ${prop.price.toLocaleString()}/=</div>
          <p>${prop.bedrooms > 0 ? prop.bedrooms + ' Beds | ' : ''}${prop.bathrooms} Baths</p>
          <p>${prop.location}</p>
          <p><em>${prop.type}</em></p>
          <a href="#" class="featured-property-btn">View Details</a>
        </div>
      </div>
    `).join('');

    revealOnScroll();
  } catch (error) {
    console.error("Error loading properties:", error);
    grid.innerHTML = "<p style='color:red; padding:20px;'>Failed to load properties. Make sure the backend is running.</p>";
  }
}

// Properties search button
function searchProperties() {
  const input = document.querySelector(".hero .search-box input");
  const query = input?.value?.trim();
  if (query) {
    loadProperties({ keyword: query });
  } else {
    loadProperties();
  }
}

// ======================================================
// APPOINTMENTS FORM - Submit appointment to backend
// ======================================================
async function submitAppointmentForm(event) {
  event.preventDefault();

  const form = event.target;

  // Read all form fields
  const appointment = {
    customerName: form.querySelector('input[placeholder="Enter your full name"]')?.value || "",
    customerEmail: form.querySelector('input[type="email"]')?.value || "",
    customerPhone: form.querySelector('input[type="tel"]')?.value || "",
    city: form.querySelector('input[placeholder="Enter your city"]')?.value || "",
    requestType: form.querySelector('select')?.value || "",
    agentName: form.querySelector('input[placeholder="Enter agent or company name"]')?.value || "",
    preferredDate: form.querySelector('input[type="date"]')?.value || "",
    preferredTime: form.querySelector('input[type="time"]')?.value || "",
    subject: form.querySelector('input[placeholder="Enter subject"]')?.value || "",
    message: form.querySelector('textarea')?.value || "",
    status: "Pending"
  };

  // Basic validation
  if (!appointment.customerName || !appointment.customerEmail) {
    alert("Please fill in your name and email.");
    return;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/appointments`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(appointment)
    });

    if (response.ok) {
      alert("✅ Your request has been submitted successfully! Our team will contact you soon.");
      form.reset();
    } else {
      alert("❌ Failed to submit request. Please try again.");
    }
  } catch (error) {
    console.error("Appointment error:", error);
    alert("❌ Could not connect to the server. Make sure the backend is running at localhost:8080.");
  }
}

// ======================================================
// CONTACT FORM - Submit contact message to backend
// ======================================================
async function submitContactForm(event) {
  event.preventDefault();

  const form = event.target;

  const message = {
    fullName: form.querySelector('input[placeholder="Enter your full name"]')?.value || "",
    email: form.querySelector('input[type="email"]')?.value || "",
    phone: form.querySelector('input[type="tel"]')?.value || "",
    city: form.querySelector('input[placeholder="Enter your city"]')?.value || "",
    inquiryType: form.querySelector('select')?.value || "",
    subject: form.querySelector('input[placeholder="Enter the subject"]')?.value || "",
    message: form.querySelector('textarea')?.value || ""
  };

  if (!message.fullName || !message.email) {
    alert("Please fill in your name and email.");
    return;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/contact-messages`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(message)
    });

    if (response.ok) {
      alert("✅ Your message has been sent! We will get back to you as soon as possible.");
      form.reset();
    } else {
      alert("❌ Failed to send message. Please try again.");
    }
  } catch (error) {
    console.error("Contact error:", error);
    alert("❌ Could not connect to the server. Make sure the backend is running at localhost:8080.");
  }
}

// ======================================================
// USER REGISTER (signup modal)
// ======================================================
async function registerUser(name, email, phone, password) {
  try {
    const response = await fetch(`${API_BASE_URL}/users/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ name, email, phone, password, role: "USER" })
    });

    const data = await response.json();

    if (response.ok) {
      alert("✅ Account created successfully! You can now log in.");
      closeModal();
    } else {
      alert("❌ Registration failed: " + (data.error || "Please try again."));
    }
  } catch (error) {
    console.error("Register error:", error);
    alert("❌ Could not connect to the server.");
  }
}

// ======================================================
// USER LOGIN
// ======================================================
async function loginUser(email, password) {
  try {
    const response = await fetch(`${API_BASE_URL}/users/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password })
    });

    const data = await response.json();

    if (response.ok) {
      alert("✅ Login successful! Welcome, " + data.name);
      sessionStorage.setItem("loggedInUser", JSON.stringify(data));
      closeModal();
    } else {
      alert("❌ Login failed: " + (data.error || "Invalid email or password."));
    }
  } catch (error) {
    console.error("Login error:", error);
    alert("❌ Could not connect to the server.");
  }
}

// ======================================================
// PAGE INITIALIZATION - runs when the page loads
// ======================================================
document.addEventListener("DOMContentLoaded", function () {

  const page = window.location.pathname;

  // Auto-load agents on agents.html
  if (page.includes("agents.html")) {
    loadAgents();

    // Attach search button
    const searchBtn = document.querySelector(".hero .search-box button");
    if (searchBtn) searchBtn.addEventListener("click", searchAgents);

    // Check if agent name is in URL (from Book Appointment button)
    const urlParams = new URLSearchParams(window.location.search);
    const agentQuery = urlParams.get("agent");
    if (agentQuery) loadAgents({ location: agentQuery });
  }

  // Auto-load properties on properties.html
  if (page.includes("properties.html")) {
    const urlParams = new URLSearchParams(window.location.search);
    const keyword = urlParams.get("keyword");

    if (keyword) {
      loadProperties({ keyword });
    } else {
      loadProperties();
    }

    // Attach search button
    const searchBtn = document.querySelector(".hero .search-box button");
    if (searchBtn) searchBtn.addEventListener("click", searchProperties);
  }

  // Attach appointment form submit
  const appointmentForm = document.querySelector(".support-form-grid");
  if (appointmentForm) {
    // Pre-fill agent name from URL if coming from agents page
    const urlParams = new URLSearchParams(window.location.search);
    const agentName = urlParams.get("agent");
    if (agentName) {
      const agentInput = appointmentForm.querySelector('input[placeholder="Enter agent or company name"]');
      if (agentInput) agentInput.value = decodeURIComponent(agentName);
    }

    appointmentForm.addEventListener("submit", submitAppointmentForm);
  }

  // Attach contact form submit
  const contactForm = document.querySelector(".contact-form-grid");
  if (contactForm) {
    contactForm.addEventListener("submit", submitContactForm);
  }
});
