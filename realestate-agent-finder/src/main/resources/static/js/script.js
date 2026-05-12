// -------------------------------------------------------
// RealtorSL - Frontend API Connection Script
// Works with Spring Boot backend at http://localhost:8080/api
// -------------------------------------------------------

const API_BASE_URL = "http://localhost:8080/api";
let authMode = "login";

// ======================================================
// SMALL HELPERS
// ======================================================
function $(selector) {
  return document.querySelector(selector);
}

function getLoggedInUser() {
  try {
    return JSON.parse(sessionStorage.getItem("loggedInUser"));
  } catch (e) {
    return null;
  }
}

function escapeHtml(value) {
  return String(value ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

async function apiRequest(path, options = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: { "Content-Type": "application/json", ...(options.headers || {}) },
    ...options
  });

  let data = null;
  try {
    data = await response.json();
  } catch (e) {
    data = null;
  }

  if (!response.ok) {
    throw new Error(data?.error || data?.message || `Request failed: ${response.status}`);
  }
  return data;
}

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
// NAV / SEARCH
// ======================================================
function searchHome() {
  const query = $(".search-box input")?.value?.trim();
  if (query) {
    window.location.href = `properties.html?keyword=${encodeURIComponent(query)}`;
  } else {
    const agentsSection = document.getElementById("homeAgentsSection") || document.querySelector(".premium-agents-section");
    if (agentsSection) agentsSection.scrollIntoView({ behavior: "smooth", block: "start" });
  }
}

function goToProperties() {
  window.location.href = "properties.html";
}

function scrollLocations(direction) {
  const slider = document.getElementById("locationsScroll");
  if (slider) slider.scrollBy({ left: direction * 300, behavior: "smooth" });
}

// ======================================================
// AUTH MODAL / LOGIN / SIGNUP
// ======================================================
function openModal(type = "login") {
  authMode = type === "signup" ? "signup" : "login";
  const modal = document.getElementById("authModal");
  if (!modal) return;

  const isSignup = authMode === "signup";
  $("#authTitle").textContent = isSignup ? "Create an account" : "Log in";
  $("#authSubtitle").textContent = isSignup
    ? "Create a user account to book appointments."
    : "Use admin@gmail.com / admin123 for admin dashboard.";
  $("#signupFields").style.display = isSignup ? "block" : "none";
  $("#continueBtn").textContent = isSignup ? "Sign up" : "Log in";
  $("#switchText").textContent = isSignup ? "Already have an account?" : "Don't have an account?";
  $("#switchAuthLink").textContent = isSignup ? "Log in" : "Sign up";

  modal.classList.add("show");
}

function closeModal() {
  const modal = document.getElementById("authModal");
  if (modal) modal.classList.remove("show");
}

async function registerUser(name, email, phone, password) {
  const data = await apiRequest("/users/register", {
    method: "POST",
    body: JSON.stringify({ name, email, phone, password, role: "USER" })
  });

  alert("✅ Account created successfully. Now log in.");
  openModal("login");
  $("#authEmail").value = email;
  $("#authPassword").value = "";
  return data;
}

async function loginUser(email, password) {
  const data = await apiRequest("/users/login", {
    method: "POST",
    body: JSON.stringify({ email, password })
  });

  sessionStorage.setItem("loggedInUser", JSON.stringify(data));
  alert("✅ Login successful! Welcome, " + data.name);
  closeModal();
  updateAuthUI();

  if (data.role === "ADMIN") {
    window.location.href = "admin.html";
  }
  return data;
}

function logout() {
  sessionStorage.removeItem("loggedInUser");
  alert("Logged out successfully.");
  window.location.href = "index.html";
}

function updateAuthUI() {
  const authButtons = document.getElementById("authButtons") || document.querySelector(".auth-buttons");
  if (!authButtons) return;

  const user = getLoggedInUser();
  if (!user) {
    authButtons.innerHTML = `
      <button class="login-btn" onclick="openModal('login')">Log in</button>
      <button class="signup-btn" onclick="openModal('signup')">Sign up</button>
    `;
    return;
  }

  authButtons.innerHTML = `
    <span style="margin-right:10px; font-weight:600;">Hi, ${escapeHtml(user.name)}</span>
    ${user.role === "ADMIN" ? '<a href="admin.html" class="login-btn" style="text-decoration:none;">Admin</a>' : ""}
    <button class="signup-btn" onclick="logout()">Logout</button>
  `;
}

function setupAuthForm() {
  const form = document.getElementById("authForm");
  if (!form) return;

  const switchLink = document.getElementById("switchAuthLink");
  if (switchLink) {
    switchLink.addEventListener("click", (event) => {
      event.preventDefault();
      openModal(authMode === "login" ? "signup" : "login");
    });
  }

  form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const email = document.getElementById("authEmail").value.trim();
    const password = document.getElementById("authPassword").value.trim();

    if (!email || !password) {
      alert("Please enter email and password.");
      return;
    }

    try {
      if (authMode === "signup") {
        const name = document.getElementById("authName").value.trim();
        const phone = document.getElementById("authPhone").value.trim();
        if (!name || !phone) {
          alert("Please enter name and phone number.");
          return;
        }
        await registerUser(name, email, phone, password);
      } else {
        await loginUser(email, password);
      }
    } catch (error) {
      alert("❌ " + error.message);
    }
  });
}

// ======================================================
// AGENTS PAGE
// ======================================================
async function loadAgents(searchParams = {}) {
  const grid = document.querySelector(".agents-list-grid");
  if (!grid) return;

  grid.innerHTML = "<p style='color:#888; padding:20px;'>Loading agents...</p>";

  try {
    let url = "/agents";
    const params = new URLSearchParams(searchParams);
    if ([...params].length > 0) url += `?${params.toString()}`;

    const agents = await apiRequest(url);

    if (!agents.length) {
      grid.innerHTML = "<p style='color:#888; padding:20px;'>No agents found.</p>";
      return;
    }

    grid.innerHTML = agents.map(agent => `
      <div class="agent-profile-card reveal">
        <img src="${escapeHtml(agent.imageUrl || 'images/john.jpg')}" alt="${escapeHtml(agent.name)}" onerror="this.src='images/john.jpg'" />
        <div class="agent-profile-content">
          <span class="agent-badge">${escapeHtml(agent.badge || 'Verified')}</span>
          <h3>${escapeHtml(agent.name)}</h3>
          <p><strong>Location:</strong> ${escapeHtml(agent.location)}</p>
          <p><strong>Specialty:</strong> ${escapeHtml(agent.specialization)}</p>
          <p><strong>Experience:</strong> ${escapeHtml(agent.experienceYears)} Years</p>
          <p class="agent-description">${escapeHtml(agent.description || '')}</p>
          <div class="agent-stars">${'★'.repeat(Math.round(agent.rating || 5))}</div>
          <a href="appointments.html?agent=${encodeURIComponent(agent.name)}" class="agent-action-btn">Book Appointment</a>
        </div>
      </div>
    `).join("");

    revealOnScroll();
  } catch (error) {
    console.error("Error loading agents:", error);
    grid.innerHTML = "<p style='color:red; padding:20px;'>Failed to load agents. Make sure backend is running.</p>";
  }
}

function searchAgents() {
  const query = document.querySelector(".hero .search-box input")?.value?.trim();
  loadAgents(query ? { location: query } : {});
}

// ======================================================
// PROPERTIES PAGE
// ======================================================
async function loadProperties(searchParams = {}) {
  const grid = document.querySelector(".featured-properties-grid");
  if (!grid) return;

  // Do not replace homepage featured static cards. Only load on properties.html.
  if (!window.location.pathname.includes("properties.html")) return;

  grid.innerHTML = "<p style='color:#888; padding:20px;'>Loading properties...</p>";

  try {
    let url = "/properties";
    const params = new URLSearchParams(searchParams);
    if ([...params].length > 0) url += `?${params.toString()}`;

    const properties = await apiRequest(url);

    if (!properties.length) {
      grid.innerHTML = "<p style='color:#888; padding:20px;'>No properties found.</p>";
      return;
    }

    grid.innerHTML = properties.map(prop => `
      <div class="featured-property-card reveal">
        <img src="${escapeHtml(prop.imageUrl || 'images/house.jpg')}" alt="${escapeHtml(prop.title)}" onerror="this.src='images/house.jpg'" />
        <div class="featured-property-content">
          <h3>${escapeHtml(prop.title)}</h3>
          <div class="featured-price">Rs. ${Number(prop.price || 0).toLocaleString()}/=</div>
          <p>${Number(prop.bedrooms || 0) > 0 ? escapeHtml(prop.bedrooms) + ' Beds | ' : ''}${escapeHtml(prop.bathrooms)} Baths</p>
          <p>${escapeHtml(prop.location)}</p>
          <p><em>${escapeHtml(prop.type)}</em></p>
          <a href="appointments.html?agent=${encodeURIComponent(prop.agentName || '')}" class="featured-property-btn">Book Viewing</a>
        </div>
      </div>
    `).join("");

    revealOnScroll();
  } catch (error) {
    console.error("Error loading properties:", error);
    grid.innerHTML = "<p style='color:red; padding:20px;'>Failed to load properties. Make sure backend is running.</p>";
  }
}

function searchProperties() {
  const query = document.querySelector(".hero .search-box input")?.value?.trim();
  loadProperties(query ? { keyword: query } : {});
}

// ======================================================
// APPOINTMENTS / CONTACT
// ======================================================
async function submitAppointmentForm(event) {
  event.preventDefault();
  const form = event.target;

  const getByName = (name) => form.querySelector(`[name="${name}"]`)?.value?.trim() || "";
  const getByPlaceholder = (selector) => form.querySelector(selector)?.value?.trim() || "";

  const appointment = {
    customerName: getByName("customerName") || getByPlaceholder('input[placeholder="Enter your full name"]'),
    customerEmail: getByName("customerEmail") || getByPlaceholder('input[type="email"]'),
    customerPhone: getByName("customerPhone") || getByPlaceholder('input[type="tel"]'),
    city: getByName("city") || getByPlaceholder('input[placeholder="Enter your city"]'),
    requestType: getByName("requestType") || form.querySelector("select")?.value || "Book Appointment",
    agentName: getByName("agentName") || getByPlaceholder('input[placeholder="Enter agent or company name"]'),
    preferredDate: getByName("preferredDate") || form.querySelector('input[type="date"]')?.value || "",
    preferredTime: getByName("preferredTime") || form.querySelector('input[type="time"]')?.value || "",
    subject: getByName("subject") || getByPlaceholder('input[placeholder="Enter subject"]') || "Appointment Request",
    message: getByName("message") || form.querySelector("textarea")?.value?.trim() || "",
    status: "Pending"
  };

  if (!appointment.customerName || !appointment.customerEmail || !appointment.customerPhone) {
    showFormStatus(form, "Please fill your name, email, and phone number.", "error");
    return;
  }

  if (!appointment.requestType || appointment.requestType === "Select request type") {
    appointment.requestType = "Book Appointment";
  }

  try {
    await apiRequest("/appointments", {
      method: "POST",
      body: JSON.stringify(appointment)
    });
    showFormStatus(form, "✅ Appointment request sent successfully. Admin can now see it in the dashboard.", "success");
    alert("✅ Appointment request sent successfully.");
    form.reset();
  } catch (error) {
    console.error("Appointment submit error:", error);
    showFormStatus(form, "❌ Failed to send appointment. Check backend is running at http://localhost:8080", "error");
    alert("❌ Failed to send appointment: " + error.message);
  }
}

function showFormStatus(form, message, type) {
  let box = form.querySelector(".form-status-message");
  if (!box) {
    box = document.createElement("div");
    box.className = "form-status-message";
    form.appendChild(box);
  }
  box.className = `form-status-message ${type}`;
  box.textContent = message;
}

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
    await apiRequest("/contact-messages", { method: "POST", body: JSON.stringify(message) });
    alert("✅ Message sent successfully.");
    form.reset();
  } catch (error) {
    alert("❌ " + error.message);
  }
}

// ======================================================
// ADMIN PAGE
// ======================================================
function requireAdmin() {
  const user = getLoggedInUser();
  if (!user || user.role !== "ADMIN") {
    alert("Please log in as admin first. Email: admin@gmail.com Password: admin123");
    window.location.href = "index.html";
    return false;
  }
  return true;
}

async function addAgent(event) {
  event.preventDefault();
  const form = event.target;

  const agent = {
    name: form.agentName.value.trim(),
    email: form.agentEmail.value.trim(),
    phone: form.agentPhone.value.trim(),
    location: form.agentLocation.value.trim(),
    specialization: form.agentSpecialization.value.trim(),
    experienceYears: Number(form.experienceYears.value || 0),
    imageUrl: form.imageUrl.value.trim() || "images/agent1.jpg",
    rating: Number(form.rating.value || 5),
    badge: form.badge.value.trim() || "Verified",
    description: form.description.value.trim()
  };

  try {
    await apiRequest("/agents", { method: "POST", body: JSON.stringify(agent) });
    alert("✅ Agent added successfully.");
    form.reset();
    loadAdminAgents();
  } catch (error) {
    alert("❌ Failed to add agent: " + error.message);
  }
}

async function addProperty(event) {
  event.preventDefault();
  const form = event.target;

  const property = {
    title: form.title.value.trim(),
    type: form.type.value.trim(),
    location: form.location.value.trim(),
    price: Number(form.price.value || 0),
    bedrooms: Number(form.bedrooms.value || 0),
    bathrooms: Number(form.bathrooms.value || 0),
    description: form.description.value.trim(),
    imageUrl: form.imageUrl.value.trim() || "images/property1.jpg",
    agentName: form.agentName.value.trim(),
    status: form.status.value.trim() || "Available"
  };

  try {
    await apiRequest("/properties", { method: "POST", body: JSON.stringify(property) });
    alert("✅ Property added successfully.");
    form.reset();
    loadAdminProperties();
  } catch (error) {
    alert("❌ Failed to add property: " + error.message);
  }
}

async function loadAdminAgents() {
  const box = document.getElementById("adminAgentsList");
  if (!box) return;
  try {
    const agents = await apiRequest("/agents");
    box.innerHTML = agents.map(a => `
      <div class="admin-item">
        <strong>${escapeHtml(a.name)}</strong><br>
        ${escapeHtml(a.location)} | ${escapeHtml(a.specialization)} | ${escapeHtml(a.email)}
        <button onclick="deleteAgent(${a.id})">Delete</button>
      </div>
    `).join("") || "<p>No agents found.</p>";
  } catch (e) {
    box.innerHTML = "<p style='color:red;'>Failed to load agents.</p>";
  }
}

async function loadAdminProperties() {
  const box = document.getElementById("adminPropertiesList");
  if (!box) return;
  try {
    const properties = await apiRequest("/properties");
    box.innerHTML = properties.map(p => `
      <div class="admin-item">
        <strong>${escapeHtml(p.title)}</strong><br>
        ${escapeHtml(p.location)} | Rs. ${Number(p.price || 0).toLocaleString()} | ${escapeHtml(p.status)}
        <button onclick="deleteProperty(${p.id})">Delete</button>
      </div>
    `).join("") || "<p>No properties found.</p>";
  } catch (e) {
    box.innerHTML = "<p style='color:red;'>Failed to load properties.</p>";
  }
}

async function loadAdminAppointments() {
  const box = document.getElementById("adminAppointmentsList");
  if (!box) return;
  box.innerHTML = "Loading appointment requests...";
  try {
    const appointments = await apiRequest("/appointments");
    if (!appointments || appointments.length === 0) {
      box.innerHTML = "<p>No appointment requests found.</p>";
      return;
    }
    box.innerHTML = appointments.map(a => `
      <div class="admin-item">
        <strong>${escapeHtml(a.customerName || "Unknown User")}</strong>
        <span class="admin-status">${escapeHtml(a.status || "Pending")}</span><br>
        Agent/Company: ${escapeHtml(a.agentName || "Not selected")}<br>
        Date: ${escapeHtml(a.preferredDate || "Not selected")} ${escapeHtml(a.preferredTime || "")}<br>
        Contact: ${escapeHtml(a.customerEmail || "")} | ${escapeHtml(a.customerPhone || "")}<br>
        City: ${escapeHtml(a.city || "")} | Type: ${escapeHtml(a.requestType || "Book Appointment")}
        <small><strong>Subject:</strong> ${escapeHtml(a.subject || "")}</small>
        <small><strong>Message:</strong> ${escapeHtml(a.message || "")}</small>
      </div>
    `).join("");
  } catch (e) {
    console.error("Admin appointments error:", e);
    box.innerHTML = "<p style='color:red;'>Failed to load appointments. Check GET http://localhost:8080/api/appointments</p>";
  }
}

async function deleteAgent(id) {
  if (!confirm("Delete this agent?")) return;
  try {
    await fetch(`${API_BASE_URL}/agents/${id}`, { method: "DELETE" });
    alert("Agent deleted.");
    loadAdminAgents();
  } catch (e) {
    alert("Failed to delete agent.");
  }
}

async function deleteProperty(id) {
  if (!confirm("Delete this property?")) return;
  try {
    await fetch(`${API_BASE_URL}/properties/${id}`, { method: "DELETE" });
    alert("Property deleted.");
    loadAdminProperties();
  } catch (e) {
    alert("Failed to delete property.");
  }
}

function setupAdminPage() {
  if (!document.getElementById("adminDashboard")) return;
  if (!requireAdmin()) return;

  const user = getLoggedInUser();
  const welcome = document.getElementById("adminWelcome");
  if (welcome) welcome.textContent = "Welcome, " + user.name;

  document.getElementById("addAgentForm")?.addEventListener("submit", addAgent);
  document.getElementById("addPropertyForm")?.addEventListener("submit", addProperty);

  loadAdminAgents();
  loadAdminProperties();
  loadAdminAppointments();
}


function setupHomeSmoothLinks() {
  document.querySelectorAll('a[href^="#"]').forEach((link) => {
    link.addEventListener("click", (event) => {
      const target = document.querySelector(link.getAttribute("href"));
      if (target) {
        event.preventDefault();
        target.scrollIntoView({ behavior: "smooth", block: "start" });
      }
    });
  });
}

function setupAppointmentPageHelpers() {
  const form = document.querySelector(".support-form-grid");
  if (!form) return;

  const params = new URLSearchParams(window.location.search);
  const agentName = params.get("agent");
  if (agentName) {
    const agentInput = form.querySelector('input[placeholder="Enter agent or company name"]') || form.querySelector('[name="agentName"]');
    if (agentInput) agentInput.value = decodeURIComponent(agentName);
    const requestSelect = form.querySelector("select");
    if (requestSelect && (!requestSelect.value || requestSelect.value === "Select request type")) {
      requestSelect.value = "Book Appointment";
    }
    setTimeout(() => form.scrollIntoView({ behavior: "smooth", block: "start" }), 250);
  }
}

// ======================================================
// PAGE INITIALIZATION
// ======================================================
document.addEventListener("DOMContentLoaded", function () {
  updateAuthUI();
  setupAuthForm();
  setupHomeSmoothLinks();
  setupAppointmentPageHelpers();

  const page = window.location.pathname;

  if (page.includes("agents.html")) {
    loadAgents();
    const searchBtn = document.querySelector(".hero .search-box button");
    if (searchBtn) searchBtn.addEventListener("click", searchAgents);
  }

  if (page.includes("properties.html")) {
    const urlParams = new URLSearchParams(window.location.search);
    const keyword = urlParams.get("keyword");
    loadProperties(keyword ? { keyword } : {});

    const searchBtn = document.querySelector(".hero .search-box button");
    if (searchBtn) searchBtn.addEventListener("click", searchProperties);
  }

  const appointmentForm = document.querySelector(".support-form-grid");
  if (appointmentForm) {
    const urlParams = new URLSearchParams(window.location.search);
    const agentName = urlParams.get("agent");
    if (agentName) {
      const agentInput = appointmentForm.querySelector('input[placeholder="Enter agent or company name"]');
      if (agentInput) agentInput.value = decodeURIComponent(agentName);
    }
    appointmentForm.addEventListener("submit", submitAppointmentForm);
  }

  const contactForm = document.querySelector(".contact-form-grid");
  if (contactForm) contactForm.addEventListener("submit", submitContactForm);

  setupAdminPage();
});
