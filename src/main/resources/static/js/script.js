// -------------------------------------------------------
// RealtorSL - Fixed Frontend API Connection Script
// Spring Boot backend: http://localhost:8080/api
// -------------------------------------------------------
const API_BASE_URL = "http://localhost:8080/api";
let authMode = "login";

function $(selector) { return document.querySelector(selector); }
function escapeHtml(value) {
  if (value === null || value === undefined) return "";
  return String(value).replace(/[&<>'"]/g, c => ({"&":"&amp;","<":"&lt;",">":"&gt;","'":"&#039;",'"':"&quot;"}[c]));
}
function getLoggedInUser() {
  try { return JSON.parse(sessionStorage.getItem("loggedInUser")); } catch { return null; }
}
async function apiRequest(path, options = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: { "Content-Type": "application/json", ...(options.headers || {}) },
    ...options
  });
  if (!response.ok) {
    let text = await response.text();
    throw new Error(text || `HTTP ${response.status}`);
  }
  if (response.status === 204) return null;
  const text = await response.text();
  return text ? JSON.parse(text) : null;
}

function revealOnScroll() {
  const revealElements = document.querySelectorAll(".reveal");
  if (!revealElements.length) return;

  if (!("IntersectionObserver" in window)) {
    revealElements.forEach(el => el.classList.add("visible", "active"));
    return;
  }

  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add("visible", "active");
        observer.unobserve(entry.target);
      }
    });
  }, { threshold: 0.12 });

  revealElements.forEach(el => {
    if (!el.dataset.revealObserved) {
      el.dataset.revealObserved = "true";
      observer.observe(el);
    }
  });
}
function searchHome() {
  const input = document.querySelector(".hero .search-box input");
  const keyword = input ? input.value.trim() : "";
  window.location.href = keyword ? `properties.html?keyword=${encodeURIComponent(keyword)}` : "properties.html";
}
function goToProperties() { window.location.href = "properties.html"; }
function scrollLocations(direction) {
  const container = document.getElementById("locationsScroll");
  if (container) container.scrollBy({ left: direction * 320, behavior: "smooth" });
}

// AUTH
function openModal(type = "login") {
  authMode = type;
  const modal = document.getElementById("authModal");
  const title = document.getElementById("authTitle");
  const subtitle = document.getElementById("authSubtitle");
  const signupFields = document.getElementById("signupFields");
  const button = document.getElementById("continueBtn");
  const switchText = document.getElementById("switchText");
  const switchLink = document.getElementById("switchAuthLink");
  if (!modal) return;
  modal.classList.add("active", "show");
  if (type === "signup") {
    if (title) title.textContent = "Sign up";
    if (subtitle) subtitle.textContent = "Create your RealtorSL account.";
    if (signupFields) signupFields.style.display = "block";
    if (button) button.textContent = "Sign up";
    if (switchText) switchText.textContent = "Already have an account?";
    if (switchLink) switchLink.textContent = "Log in";
  } else {
    if (title) title.textContent = "Log in";
    if (subtitle) subtitle.textContent = "Use your account details to continue.";
    if (signupFields) signupFields.style.display = "none";
    if (button) button.textContent = "Log in";
    if (switchText) switchText.textContent = "Don't have an account?";
    if (switchLink) switchLink.textContent = "Sign up";
  }
}
function closeModal() { document.getElementById("authModal")?.classList.remove("active", "show"); }
async function registerUser(name, email, phone, password) {
  const data = await apiRequest("/users/register", { method: "POST", body: JSON.stringify({ name, email, phone, password, role: "USER" }) });
  sessionStorage.setItem("loggedInUser", JSON.stringify(data));
  updateAuthUI(); closeModal(); alert("✅ Registered successfully.");
}
async function loginUser(email, password) {
  const data = await apiRequest("/users/login", { method: "POST", body: JSON.stringify({ email, password }) });
  sessionStorage.setItem("loggedInUser", JSON.stringify(data));
  updateAuthUI(); closeModal();
  if (data.role === "ADMIN") window.location.href = "admin.html"; else alert("✅ Logged in successfully.");
}
function logout() { sessionStorage.removeItem("loggedInUser"); updateAuthUI(); window.location.href = "index.html"; }
function updateAuthUI() {
  const box = document.getElementById("authButtons");
  if (!box) return;
  const user = getLoggedInUser();
  if (!user) {
    box.innerHTML = `<button class="login-btn" onclick="openModal('login')">Log in</button><button class="signup-btn" onclick="openModal('signup')">Sign up</button>`;
    return;
  }
  box.innerHTML = `<span style="font-weight:700;color:#0b8f3a;margin-right:10px;">${escapeHtml(user.name)}</span>${user.role === "ADMIN" ? '<a href="admin.html" class="login-btn" style="text-decoration:none;">Admin</a>' : ''}<button class="signup-btn" onclick="logout()">Logout</button>`;
}
function setupAuthForm() {
  const form = document.getElementById("authForm");
  if (!form) return;
  document.getElementById("switchAuthLink")?.addEventListener("click", e => { e.preventDefault(); openModal(authMode === "login" ? "signup" : "login"); });
  form.addEventListener("submit", async e => {
    e.preventDefault();
    const email = document.getElementById("authEmail")?.value.trim();
    const password = document.getElementById("authPassword")?.value.trim();
    if (!email || !password) return alert("Please enter email and password.");
    try {
      if (authMode === "signup") {
        await registerUser(document.getElementById("authName")?.value.trim(), email, document.getElementById("authPhone")?.value.trim(), password);
      } else await loginUser(email, password);
    } catch (err) { alert("❌ " + err.message); }
  });
}


function setupModalCloseHelpers() {
  const modal = document.getElementById("authModal");
  if (!modal || modal.dataset.closeReady) return;
  modal.dataset.closeReady = "true";
  modal.addEventListener("click", (e) => {
    if (e.target === modal) closeModal();
  });
  document.addEventListener("keydown", (e) => {
    if (e.key === "Escape") closeModal();
  });
}

// MODALS
function ensureInfoModal() {
  if (document.getElementById("infoModal")) return;
  document.body.insertAdjacentHTML("beforeend", `
    <div class="profile-modal-overlay" id="infoModal">
      <div class="profile-modal-card">
        <button class="profile-modal-close" onclick="closeInfoModal()">&times;</button>
        <div id="infoModalBody"></div>
      </div>
    </div>`);
}
function openInfoModal(html) { ensureInfoModal(); document.getElementById("infoModalBody").innerHTML = html; document.getElementById("infoModal").classList.add("active"); }
function closeInfoModal() { document.getElementById("infoModal")?.classList.remove("active"); }
function goToAppointmentFor(name, type = "Book Appointment") {
  window.location.href = `appointments.html?agent=${encodeURIComponent(name || "")}&type=${encodeURIComponent(type)}`;
}
function scrollToViewingForm(propertyTitle) {
  const input = document.querySelector('[name="selectedProperty"]');
  if (input) input.value = propertyTitle || "";
  const form = document.getElementById("propertyViewingForm");
  if (form) form.scrollIntoView({ behavior: "smooth", block: "start" });
}

// AGENTS
async function loadAgents(searchParams = {}) {
  const grid = document.querySelector(".agents-list-grid");
  if (!grid) return;
  grid.innerHTML = "<p style='color:#888;padding:20px;'>Loading agents...</p>";
  try {
    const params = new URLSearchParams(searchParams); const url = "/agents" + (params.toString() ? `?${params}` : "");
    const agents = await apiRequest(url);
    grid.innerHTML = (agents || []).map(a => `
      <div class="agent-profile-card reveal">
        <img src="${escapeHtml(a.imageUrl || 'images/john.jpg')}" alt="${escapeHtml(a.name)}" onerror="this.src='images/john.jpg'" />
        <div class="agent-profile-content">
          <span class="agent-badge">${escapeHtml(a.badge || 'Verified')}</span>
          <h3>${escapeHtml(a.name)}</h3>
          <p><strong>Location:</strong> ${escapeHtml(a.location || a.city || '')}</p>
          <p><strong>Specialty:</strong> ${escapeHtml(a.specialization || '')}</p>
          <p><strong>Experience:</strong> ${escapeHtml(a.experienceYears || 0)} Years</p>
          <p class="agent-description">${escapeHtml(a.description || '')}</p>
          <div class="agent-stars">${'★'.repeat(Math.max(1, Math.round(a.rating || 5)))}</div>
          <button class="agent-action-btn" onclick='displayAgentProfile(${JSON.stringify(a).replace(/'/g,"&#039;")})'>Display Profile</button>
          <button class="agent-action-btn" onclick="goToAppointmentFor('${escapeHtml(a.name).replace(/'/g,"&#039;")}')">Book Appointment</button>
        </div>
      </div>`).join("") || "<p>No agents found.</p>";
    revealOnScroll();
  } catch (err) { grid.innerHTML = "<p style='color:red;padding:20px;'>Failed to load agents. Make sure backend is running.</p>"; }
}
function displayAgentProfile(a) {
  openInfoModal(`
    <img class="profile-modal-img" src="${escapeHtml(a.imageUrl || 'images/john.jpg')}" onerror="this.src='images/john.jpg'" alt="${escapeHtml(a.name)}">
    <h2>${escapeHtml(a.name)}</h2>
    <div class="agent-stars">${'★'.repeat(Math.max(1, Math.round(a.rating || 5)))}</div>
    <p><strong>Email:</strong> ${escapeHtml(a.email || '')}</p>
    <p><strong>Phone:</strong> ${escapeHtml(a.phone || '')}</p>
    <p><strong>City:</strong> ${escapeHtml(a.location || a.city || '')}</p>
    <p><strong>Company:</strong> ${escapeHtml(a.companyName || 'Independent Agent')}</p>
    <p><strong>Experience:</strong> ${escapeHtml(a.experienceYears || 0)} Years</p>
    <p><strong>Specialization:</strong> ${escapeHtml(a.specialization || '')}</p>
    <p>${escapeHtml(a.description || '')}</p>
    <button class="agent-action-btn" onclick="goToAppointmentFor('${escapeHtml(a.name).replace(/'/g,"&#039;")}')">Book Appointment</button>`);
}
function searchAgents() {
  const query = document.querySelector(".hero .search-box input")?.value.trim();
  loadAgents(query ? { location: query } : {});
}

// COMPANIES

function getCompanyCardData(card) {
  const getText = (selector) => card.querySelector(selector)?.textContent.trim() || "";
  const paragraphs = Array.from(card.querySelectorAll("p"));
  const findValue = (label) => {
    const row = paragraphs.find(p => p.textContent.trim().toLowerCase().startsWith(label.toLowerCase() + ":"));
    return row ? row.textContent.replace(new RegExp("^" + label + ":", "i"), "").trim() : "";
  };
  return {
    name: getText("h3"),
    city: findValue("Location"),
    email: findValue("Email"),
    phone: findValue("Phone"),
    address: findValue("Address"),
    imageUrl: card.querySelector("img")?.getAttribute("src") || "images/meeting.jpg",
    badge: getText(".company-badge") || "Verified Network",
    description: getText(".company-description")
  };
}

function enhanceStaticCompanyCards() {
  const grid = document.querySelector(".companies-grid");
  if (!grid) return false;
  const cards = Array.from(grid.querySelectorAll(".company-card"));
  if (!cards.length) return false;

  cards.forEach(card => {
    const company = getCompanyCardData(card);
    const content = card.querySelector(".company-content");
    if (!content || !company.name) return;

    // Keep the exact card layout, but make the buttons work with the same actions as dynamic backend cards.
    content.querySelectorAll(".company-btn").forEach(btn => btn.remove());

    const displayBtn = document.createElement("button");
    displayBtn.type = "button";
    displayBtn.className = "company-btn";
    displayBtn.textContent = "Display Profile";
    displayBtn.addEventListener("click", () => displayCompanyProfile(company));

    const appointmentBtn = document.createElement("button");
    appointmentBtn.type = "button";
    appointmentBtn.className = "company-btn";
    appointmentBtn.textContent = "Book Appointment";
    appointmentBtn.addEventListener("click", () => goToAppointmentFor(company.name, "Company Appointment"));

    content.appendChild(displayBtn);
    content.appendChild(appointmentBtn);
  });

  revealOnScroll();
  return true;
}

async function loadCompanies(searchParams = {}) {
  const grid = document.querySelector(".companies-grid");
  if (!grid) return;

  // IMPORTANT: Do not overwrite the company cards written in agents.html.
  // This lets your edited names, images, and information show exactly as you wrote them,
  // while keeping the same Display Profile and Book Appointment buttons.
  if (grid.querySelector(".company-card")) {
    enhanceStaticCompanyCards();
    return;
  }

  grid.innerHTML = "<p style='color:#888;padding:20px;'>Loading companies...</p>";
  try {
    const params = new URLSearchParams(searchParams); const url = "/companies" + (params.toString() ? `?${params}` : "");
    const companies = await apiRequest(url);
    grid.innerHTML = (companies || []).map(c => `
      <div class="company-card reveal">
        <img src="${escapeHtml(c.imageUrl || 'images/meeting.jpg')}" alt="${escapeHtml(c.name)}" onerror="this.src='images/meeting.jpg'" />
        <div class="company-content">
          <span class="company-badge">${escapeHtml(c.badge || 'Verified Network')}</span>
          <h3>${escapeHtml(c.name)}</h3>
          <p><strong>Location:</strong> ${escapeHtml(c.city || '')}</p>
          <p><strong>Email:</strong> ${escapeHtml(c.email || '')}</p>
          <p class="company-description">${escapeHtml(c.description || '')}</p>
          <button class="company-btn" onclick='displayCompanyProfile(${JSON.stringify(c).replace(/'/g,"&#039;")})'>Display Profile</button>
          <button class="company-btn" onclick="goToAppointmentFor('${escapeHtml(c.name).replace(/'/g,"&#039;")}', 'Company Appointment')">Book Appointment</button>
        </div>
      </div>`).join("") || "<p>No companies found.</p>";
    revealOnScroll();
  } catch { grid.innerHTML = "<p style='color:red;padding:20px;'>Failed to load companies.</p>"; }
}
function displayCompanyProfile(c) {
  openInfoModal(`
    <img class="profile-modal-img" src="${escapeHtml(c.imageUrl || 'images/meeting.jpg')}" onerror="this.src='images/meeting.jpg'" alt="${escapeHtml(c.name)}">
    <h2>${escapeHtml(c.name)}</h2>
    <p><strong>City:</strong> ${escapeHtml(c.city || '')}</p>
    <p><strong>Address:</strong> ${escapeHtml(c.address || '')}</p>
    <p><strong>Email:</strong> ${escapeHtml(c.email || '')}</p>
    <p><strong>Phone:</strong> ${escapeHtml(c.phone || '')}</p>
    <p>${escapeHtml(c.description || '')}</p>
    <p><strong>Agents:</strong> Company agents can be assigned through admin records.</p>
    <button class="agent-action-btn" onclick="goToAppointmentFor('${escapeHtml(c.name).replace(/'/g,"&#039;")}', 'Company Appointment')">Book Appointment</button>`);
}

// PROPERTIES
async function loadProperties(searchParams = {}) {
  const grid = document.querySelector(".featured-properties-grid");
  if (!grid || !window.location.pathname.includes("properties.html")) return;
  grid.innerHTML = "<p style='color:#888;padding:20px;'>Loading properties...</p>";
  try {
    const params = new URLSearchParams(searchParams); const url = "/properties" + (params.toString() ? `?${params}` : "");
    const properties = await apiRequest(url);
    grid.innerHTML = (properties || []).map(p => `
      <div class="featured-property-card reveal">
        <img src="${escapeHtml(p.imageUrl || 'images/house.jpg')}" alt="${escapeHtml(p.title)}" onerror="this.src='images/house.jpg'" />
        <div class="featured-property-content">
          <h3>${escapeHtml(p.title)}</h3>
          <div class="featured-price">Rs. ${Number(p.price || 0).toLocaleString()}/=</div>
          <p>${Number(p.bedrooms || 0) > 0 ? escapeHtml(p.bedrooms) + ' Beds | ' : ''}${escapeHtml(p.bathrooms || 0)} Baths</p>
          <p>${escapeHtml(p.location || '')}</p>
          <p><em>${escapeHtml(p.type || '')}</em></p>
          <button class="featured-property-btn" onclick='displayProperty(${JSON.stringify(p).replace(/'/g,"&#039;")})'>Display Property</button>
          <button class="featured-property-btn" onclick="scrollToViewingForm('${escapeHtml(p.title).replace(/'/g,"&#039;")}')">Book Viewing</button>
        </div>
      </div>`).join("") || "<p>No properties found.</p>";
    revealOnScroll();
  } catch { grid.innerHTML = "<p style='color:red;padding:20px;'>Failed to load properties. Make sure backend is running.</p>"; }
}
function displayProperty(p) {
  openInfoModal(`
    <img class="profile-modal-img" src="${escapeHtml(p.imageUrl || 'images/house.jpg')}" onerror="this.src='images/house.jpg'" alt="${escapeHtml(p.title)}">
    <h2>${escapeHtml(p.title)}</h2>
    <div class="featured-price">Rs. ${Number(p.price || 0).toLocaleString()}/=</div>
    <p><strong>Category:</strong> ${escapeHtml(p.type || '')}</p>
    <p><strong>City:</strong> ${escapeHtml(p.location || '')}</p>
    <p><strong>Bedrooms/Bathrooms:</strong> ${escapeHtml(p.bedrooms || 0)} Beds | ${escapeHtml(p.bathrooms || 0)} Baths</p>
    <p><strong>Agent/Company:</strong> ${escapeHtml(p.agentName || '')}</p>
    <p>${escapeHtml(p.description || '')}</p>
    <button class="featured-property-btn" onclick="closeInfoModal(); scrollToViewingForm('${escapeHtml(p.title).replace(/'/g,"&#039;")}')">Book Viewing</button>`);
}
function searchProperties() {
  const query = document.querySelector(".hero .search-box input")?.value.trim();
  loadProperties(query ? { keyword: query } : {});
}
function filterByCategory(category) { loadProperties({ category }); document.querySelector(".featured-properties")?.scrollIntoView({ behavior: "smooth" }); }
function filterByCity(city) { loadProperties({ city }); document.querySelector(".featured-properties")?.scrollIntoView({ behavior: "smooth" }); }
function setupPropertyFilters() {
  if (!window.location.pathname.includes("properties.html")) return;
  document.querySelectorAll(".property-categories-grid .property-card").forEach(card => {
    const title = card.querySelector("h3")?.textContent.trim();
    const map = { "Houses":"House", "Apartments":"Apartment", "Lands":"Land", "Commercial":"Commercial", "Villas":"Villa", "Rentals":"Rental", "Land":"Land" };
    card.style.cursor = "pointer";
    card.addEventListener("click", () => filterByCategory(map[title] || title));
  });
  document.querySelectorAll(".location-card").forEach(card => {
    const city = card.querySelector("h3")?.textContent.trim();
    if (city) { card.style.cursor = "pointer"; card.addEventListener("click", () => filterByCity(city)); }
  });
}

// APPOINTMENTS / PROPERTY VIEWING
async function submitAppointmentForm(event) {
  event.preventDefault(); const form = event.target;
  const val = sel => form.querySelector(sel)?.value.trim() || "";
  const appointment = {
    customerName: val('[name="customerName"]') || val('input[placeholder="Enter your full name"]'),
    customerEmail: val('[name="customerEmail"]') || val('input[type="email"]'),
    customerPhone: val('[name="customerPhone"]') || val('input[type="tel"]'),
    city: val('[name="city"]') || val('input[placeholder="Enter your city"]'),
    requestType: val('[name="requestType"]') || form.querySelector("select")?.value || "Book Appointment",
    agentName: val('[name="agentName"]') || val('input[placeholder="Enter agent or company name"]'),
    preferredDate: val('[name="preferredDate"]') || form.querySelector('input[type="date"]')?.value || "",
    preferredTime: val('[name="preferredTime"]') || form.querySelector('input[type="time"]')?.value || "",
    subject: val('[name="subject"]') || val('input[placeholder="Enter subject"]') || "Appointment Request",
    message: val('[name="message"]') || form.querySelector("textarea")?.value.trim() || "",
    status: "Pending"
  };
  if (!appointment.customerName || !appointment.customerEmail || !appointment.customerPhone) return showFormStatus(form, "Please fill your name, email, and phone number.", "error");
  try { await apiRequest("/appointments", { method: "POST", body: JSON.stringify(appointment) }); showFormStatus(form, "✅ Appointment request sent successfully.", "success"); alert("✅ Appointment request sent successfully."); form.reset(); }
  catch (err) { showFormStatus(form, "❌ Failed to send appointment. Check backend.", "error"); alert("❌ " + err.message); }
}
function showFormStatus(form, message, type) {
  let box = form.querySelector(".form-status-message");
  if (!box) { box = document.createElement("div"); box.className = "form-status-message"; form.appendChild(box); }
  box.className = `form-status-message ${type}`; box.textContent = message;
}
function setupAppointmentPageHelpers() {
  const form = document.querySelector(".support-form-grid"); if (!form) return;
  const params = new URLSearchParams(window.location.search); const agentName = params.get("agent"); const type = params.get("type");
  if (agentName) {
    const agentInput = form.querySelector('input[placeholder="Enter agent or company name"]') || form.querySelector('[name="agentName"]'); if (agentInput) agentInput.value = decodeURIComponent(agentName);
    const requestSelect = form.querySelector("select"); if (requestSelect) requestSelect.value = decodeURIComponent(type || "Book Appointment");
    setTimeout(() => form.scrollIntoView({ behavior: "smooth", block: "start" }), 250);
  }
}
function ensurePropertyViewingForm() {
  if (!window.location.pathname.includes("properties.html") || document.getElementById("propertyViewingForm")) return;
  const after = document.querySelector(".featured-properties"); if (!after) return;
  after.insertAdjacentHTML("afterend", `
    <section class="support-form-section" id="propertyViewingSection">
      <div class="support-form-header">
        <span class="support-form-tag">Property Viewing</span>
        <h2>Book a property viewing</h2>
        <p>Choose a property, enter your details, and the admin will receive your viewing request.</p>
      </div>
      <div class="support-form-wrapper reveal">
        <form class="support-form-grid" id="propertyViewingForm">
          <div class="form-group"><label>Customer Name</label><input name="customerName" type="text" placeholder="Enter your full name" required></div>
          <div class="form-group"><label>Email</label><input name="customerEmail" type="email" placeholder="Enter your email" required></div>
          <div class="form-group"><label>Phone</label><input name="customerPhone" type="tel" placeholder="Enter your phone number" required></div>
          <div class="form-group"><label>Selected Property</label><input name="selectedProperty" type="text" placeholder="Selected property" required></div>
          <div class="form-group"><label>Preferred Viewing Date</label><input name="preferredViewingDate" type="date" required></div>
          <div class="form-group full-width"><label>Message</label><textarea name="message" rows="5" placeholder="Write your message here"></textarea></div>
          <div class="form-group full-width"><button type="submit" class="support-submit-btn">Submit Viewing Request</button></div>
        </form>
      </div>
    </section>`);
  document.getElementById("propertyViewingForm").addEventListener("submit", submitPropertyViewingForm);
}
async function submitPropertyViewingForm(event) {
  event.preventDefault(); const form = event.target;
  const body = {
    customerName: form.customerName.value.trim(), customerEmail: form.customerEmail.value.trim(), customerPhone: form.customerPhone.value.trim(),
    selectedProperty: form.selectedProperty.value.trim(), preferredViewingDate: form.preferredViewingDate.value, message: form.message.value.trim(), status: "Pending"
  };
  try { await apiRequest("/property-viewings", { method: "POST", body: JSON.stringify(body) }); showFormStatus(form, "✅ Property viewing request sent successfully.", "success"); alert("✅ Property viewing request sent successfully."); form.reset(); }
  catch (err) { showFormStatus(form, "❌ Failed to send viewing request.", "error"); alert("❌ " + err.message); }
}
async function submitContactForm(event) {
  event.preventDefault(); const form = event.target;
  const message = { fullName: form.querySelector('input[placeholder="Enter your full name"]')?.value || "", email: form.querySelector('input[type="email"]')?.value || "", phone: form.querySelector('input[type="tel"]')?.value || "", city: form.querySelector('input[placeholder="Enter your city"]')?.value || "", inquiryType: form.querySelector('select')?.value || "", subject: form.querySelector('input[placeholder="Enter the subject"]')?.value || "", message: form.querySelector('textarea')?.value || "" };
  if (!message.fullName || !message.email) return alert("Please fill in your name and email.");
  try { await apiRequest("/contact-messages", { method: "POST", body: JSON.stringify(message) }); alert("✅ Message sent successfully."); form.reset(); }
  catch (err) { alert("❌ " + err.message); }
}

// ADMIN
function requireAdmin() {
  const user = getLoggedInUser();
  if (!user || user.role !== "ADMIN") { alert("Please log in as admin first. Email: admin@gmail.com Password: admin123"); window.location.href = "index.html"; return false; }
  return true;
}
async function addAgent(event) {
  event.preventDefault(); const f = event.target;
  const agent = { name:f.agentName.value.trim(), email:f.agentEmail.value.trim(), phone:f.agentPhone.value.trim(), location:f.agentLocation.value.trim(), specialization:f.agentSpecialization.value.trim(), experienceYears:Number(f.experienceYears.value||0), imageUrl:f.imageUrl.value.trim()||"images/john.jpg", rating:Number(f.rating.value||5), badge:f.badge.value.trim()||"Verified", description:f.description.value.trim() };
  try { await apiRequest("/agents", { method:"POST", body:JSON.stringify(agent) }); alert("✅ Agent added."); f.reset(); loadAdminAgents(); loadAgents(); } catch(err) { alert("❌ " + err.message); }
}
async function addProperty(event) {
  event.preventDefault(); const f = event.target;
  const property = { title:f.title.value.trim(), type:f.type.value.trim(), location:f.location.value.trim(), price:Number(f.price.value||0), bedrooms:Number(f.bedrooms.value||0), bathrooms:Number(f.bathrooms.value||0), description:f.description.value.trim(), imageUrl:f.imageUrl.value.trim()||"images/house.jpg", agentName:f.agentName.value.trim(), status:f.status.value.trim()||"Available" };
  try { await apiRequest("/properties", { method:"POST", body:JSON.stringify(property) }); alert("✅ Property added."); f.reset(); loadAdminProperties(); } catch(err) { alert("❌ " + err.message); }
}
async function addCompany(event) {
  event.preventDefault(); const f = event.target;
  const company = { name:f.companyName.value.trim(), city:f.companyCity.value.trim(), address:f.companyAddress.value.trim(), email:f.companyEmail.value.trim(), phone:f.companyPhone.value.trim(), imageUrl:f.companyImageUrl.value.trim()||"images/meeting.jpg", badge:f.companyBadge.value.trim()||"Verified Network", description:f.companyDescription.value.trim() };
  try { await apiRequest("/companies", { method:"POST", body:JSON.stringify(company) }); alert("✅ Company added."); f.reset(); loadAdminCompanies(); } catch(err) { alert("❌ " + err.message); }
}
function ensureAdminCompanyAndViewingSections() {
  const dashboard = document.getElementById("adminDashboard"); if (!dashboard || document.getElementById("addCompanyForm")) return;
  const firstGrid = dashboard.querySelector(".admin-grid");
  firstGrid?.insertAdjacentHTML("beforeend", `
    <section class="admin-card reveal visible"><h2>Add Company</h2><p class="admin-card-subtitle">Create trusted company profiles.</p>
      <form class="admin-form" id="addCompanyForm">
        <div><label>Company name</label><input name="companyName" placeholder="Prime Lands Realty" required></div>
        <div><label>City</label><input name="companyCity" placeholder="Colombo" required></div>
        <div><label>Address</label><input name="companyAddress" placeholder="Company address"></div>
        <div><label>Email</label><input name="companyEmail" type="email" placeholder="company@gmail.com"></div>
        <div><label>Phone</label><input name="companyPhone" placeholder="0771234567"></div>
        <div><label>Image URL</label><input name="companyImageUrl" value="images/meeting.jpg"></div>
        <div><label>Badge</label><input name="companyBadge" value="Verified Network"></div>
        <div class="full"><label>Description</label><textarea name="companyDescription" placeholder="Company description"></textarea></div>
        <div class="full"><button class="admin-btn" type="submit">Add Company</button></div>
      </form></section>`);
  dashboard.insertAdjacentHTML("beforeend", `
    <section class="admin-card reveal visible"><h2>Current Companies</h2><p class="admin-card-subtitle">View and delete existing company profiles.</p><div class="admin-list" id="adminCompaniesList">Loading...</div></section>
    <section class="admin-card reveal visible"><h2>Property Viewing Requests</h2><p class="admin-card-subtitle">See property viewing requests submitted by users.</p><div class="admin-list" id="adminViewingList">Loading...</div></section>`);
}
async function loadAdminAgents(){ const box=document.getElementById("adminAgentsList"); if(!box)return; try{const list=await apiRequest("/agents"); box.innerHTML=(list||[]).map(a=>`<div class="admin-item"><strong>${escapeHtml(a.name)}</strong><br>${escapeHtml(a.location)} | ${escapeHtml(a.specialization)} | ${escapeHtml(a.email)}<button onclick="deleteAgent(${a.id})">Delete</button></div>`).join("")||"<p>No agents found.</p>";}catch{box.innerHTML="<p style='color:red;'>Failed to load agents.</p>";} }
async function loadAdminProperties(){ const box=document.getElementById("adminPropertiesList"); if(!box)return; try{const list=await apiRequest("/properties"); box.innerHTML=(list||[]).map(p=>`<div class="admin-item"><strong>${escapeHtml(p.title)}</strong><br>${escapeHtml(p.type)} | ${escapeHtml(p.location)} | Rs. ${Number(p.price||0).toLocaleString()} | ${escapeHtml(p.status)}<button onclick="deleteProperty(${p.id})">Delete</button></div>`).join("")||"<p>No properties found.</p>";}catch{box.innerHTML="<p style='color:red;'>Failed to load properties.</p>";} }
async function loadAdminCompanies(){ const box=document.getElementById("adminCompaniesList"); if(!box)return; try{const list=await apiRequest("/companies"); box.innerHTML=(list||[]).map(c=>`<div class="admin-item"><strong>${escapeHtml(c.name)}</strong><br>${escapeHtml(c.city)} | ${escapeHtml(c.email)} | ${escapeHtml(c.phone)}<button onclick="deleteCompany(${c.id})">Delete</button></div>`).join("")||"<p>No companies found.</p>";}catch{box.innerHTML="<p style='color:red;'>Failed to load companies.</p>";} }
async function loadAdminAppointments(){ const box=document.getElementById("adminAppointmentsList"); if(!box)return; try{const list=await apiRequest("/appointments"); box.innerHTML=(list||[]).map(a=>`<div class="admin-item"><strong>${escapeHtml(a.customerName||'Unknown')}</strong><span class="admin-status">${escapeHtml(a.status||'Pending')}</span><br>Agent/Company: ${escapeHtml(a.agentName||'Not selected')}<br>Date: ${escapeHtml(a.preferredDate||'')} ${escapeHtml(a.preferredTime||'')}<br>Contact: ${escapeHtml(a.customerEmail||'')} | ${escapeHtml(a.customerPhone||'')}<br>City: ${escapeHtml(a.city||'')} | Type: ${escapeHtml(a.requestType||'Book Appointment')}<small><strong>Subject:</strong> ${escapeHtml(a.subject||'')}</small><small><strong>Message:</strong> ${escapeHtml(a.message||'')}</small><button onclick="deleteAppointment(${a.id})">Delete</button></div>`).join("")||"<p>No appointments found.</p>";}catch{box.innerHTML="<p style='color:red;'>Failed to load appointments.</p>";} }

async function loadAdminContactMessages(){
  const box = document.getElementById("adminContactMessagesList");
  if (!box) return;

  try {
    const list = await apiRequest("/contact-messages");

    box.innerHTML = (list || []).map(m => `
      <div class="admin-item contact-message-item">
        <div class="contact-message-main">
          <h3>${escapeHtml(m.fullName || "Unknown")}</h3>

          <div class="contact-message-meta">
            <p><strong>Email:</strong> ${escapeHtml(m.email || "")}</p>
            <p><strong>Phone:</strong> ${escapeHtml(m.phone || "")}</p>
            <p><strong>City:</strong> ${escapeHtml(m.city || "")}</p>
            <p><strong>Inquiry Type:</strong> ${escapeHtml(m.inquiryType || "")}</p>
          </div>

          <div class="contact-message-body">
            <p><strong>Subject:</strong> ${escapeHtml(m.subject || "")}</p>
            <p><strong>Message:</strong> ${escapeHtml(m.message || "")}</p>
          </div>
        </div>

        <button class="contact-delete-btn" onclick="deleteContactMessage(${m.id})">Delete</button>
      </div>
    `).join("") || "<p>No contact messages found.</p>";

  } catch (error) {
    console.error("Contact messages loading error:", error);
    box.innerHTML = "<p style='color:red;'>Failed to load contact messages.</p>";
  }
}

async function deleteContactMessage(id){
  if (!id) return alert("Invalid contact message id.");
  if (!confirm("Delete this contact message?")) return;

  try {
    const response = await fetch(`${API_BASE_URL}/contact-messages/${id}`, {
      method: "DELETE"
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || `HTTP ${response.status}`);
    }

    alert("✅ Contact message deleted.");
    loadAdminContactMessages();

  } catch (error) {
    console.error("Delete contact message error:", error);
    alert("❌ Failed to delete contact message. Check backend console.");
  }
}

async function loadAdminViewings(){ const box=document.getElementById("adminViewingList"); if(!box)return; try{const list=await apiRequest("/property-viewings"); box.innerHTML=(list||[]).map(v=>`<div class="admin-item"><strong>${escapeHtml(v.customerName||'Unknown')}</strong><span class="admin-status">${escapeHtml(v.status||'Pending')}</span><br>Property: ${escapeHtml(v.selectedProperty||'')}<br>Date: ${escapeHtml(v.preferredViewingDate||'')}<br>Contact: ${escapeHtml(v.customerEmail||'')} | ${escapeHtml(v.customerPhone||'')}<small><strong>Message:</strong> ${escapeHtml(v.message||'')}</small><button onclick="deleteViewing(${v.id})">Delete</button></div>`).join("")||"<p>No property viewing requests found.</p>";}catch{box.innerHTML="<p style='color:red;'>Failed to load property viewings.</p>";} }
async function deleteAgent(id){ if(confirm("Delete this agent?")){ await fetch(`${API_BASE_URL}/agents/${id}`,{method:"DELETE"}); loadAdminAgents(); } }
async function deleteProperty(id){ if(confirm("Delete this property?")){ await fetch(`${API_BASE_URL}/properties/${id}`,{method:"DELETE"}); loadAdminProperties(); } }
async function deleteCompany(id){ if(confirm("Delete this company?")){ await fetch(`${API_BASE_URL}/companies/${id}`,{method:"DELETE"}); loadAdminCompanies(); } }
async function deleteAppointment(id){ if(confirm("Delete this appointment?")){ await fetch(`${API_BASE_URL}/appointments/${id}`,{method:"DELETE"}); loadAdminAppointments(); } }
async function deleteViewing(id){ if(confirm("Delete this viewing request?")){ await fetch(`${API_BASE_URL}/property-viewings/${id}`,{method:"DELETE"}); loadAdminViewings(); } }
function setupAdminPage() {
  if (!document.getElementById("adminDashboard")) return; if (!requireAdmin()) return;
  ensureAdminCompanyAndViewingSections();
  const user=getLoggedInUser(); const welcome=document.getElementById("adminWelcome"); if(welcome) welcome.textContent="Welcome, "+user.name;
  document.getElementById("addAgentForm")?.addEventListener("submit", addAgent);
  document.getElementById("addPropertyForm")?.addEventListener("submit", addProperty);
  document.getElementById("addCompanyForm")?.addEventListener("submit", addCompany);
  loadAdminAgents(); loadAdminProperties(); loadAdminCompanies(); loadAdminAppointments(); loadAdminContactMessages(); loadAdminViewings();
}


function scrollToHashTarget() {
  if (!window.location.hash) return;
  const target = document.querySelector(window.location.hash);
  if (!target) return;
  setTimeout(() => {
    target.scrollIntoView({ behavior: "smooth", block: "start" });
  }, 150);
}

function setupHomeSmoothLinks() { document.querySelectorAll('a[href^="#"]').forEach(link => link.addEventListener("click", e => { const target=document.querySelector(link.getAttribute("href")); if(target){e.preventDefault(); target.scrollIntoView({behavior:"smooth", block:"start"});}})); }



// -------------------------------------------------------
// Hero tabs and search actions for every main page
// -------------------------------------------------------
function smoothScrollTo(selector) {
  const target = document.querySelector(selector);
  if (target) target.scrollIntoView({ behavior: "smooth", block: "start" });
}

function setHeroActiveTab(tab) {
  const tabs = tab.closest(".tabs");
  if (!tabs) return;
  tabs.querySelectorAll("span").forEach(item => item.classList.remove("active"));
  tab.classList.add("active");
}

function getActiveHeroTabText() {
  return document.querySelector(".hero .tabs span.active")?.textContent.trim() || "";
}

function setSupportRequestType(type, subjectText = "") {
  const form = document.querySelector(".support-form-grid:not(#propertyViewingForm)");
  if (!form) return;
  const select = form.querySelector("select");
  const subjectInput = form.querySelector('input[placeholder="Enter subject"]');
  const messageBox = form.querySelector("textarea");

  const optionMap = {
    "Appointments": "Book Appointment",
    "Appointment": "Book Appointment",
    "Complaints": "Complaint",
    "Complaint": "Complaint",
    "Support": "Support Request",
    "Reschedule": "Reschedule Appointment",
    "Help": "General Inquiry"
  };

  const value = optionMap[type] || type || "General Inquiry";
  if (select) {
    const match = Array.from(select.options).find(opt => opt.textContent.trim().toLowerCase() === value.toLowerCase());
    if (match) select.value = match.value;
  }
  if (subjectInput && subjectText) subjectInput.value = subjectText;
  if (messageBox && subjectText && !messageBox.value.trim()) messageBox.value = subjectText;
  smoothScrollTo(".support-form-section");
}

function setContactInquiryType(type, subjectText = "") {
  const form = document.querySelector(".contact-form-grid");
  if (!form) return;
  const select = form.querySelector("select");
  const subjectInput = form.querySelector('input[placeholder="Enter the subject"]');
  const messageBox = form.querySelector("textarea");

  const optionMap = {
    "Contact": "General Inquiry",
    "Support": "Technical Support",
    "FAQ": "General Inquiry",
    "Office": "General Inquiry",
    "Help": "Appointment Help"
  };

  const value = optionMap[type] || "General Inquiry";
  if (select) {
    const match = Array.from(select.options).find(opt => opt.textContent.trim().toLowerCase() === value.toLowerCase());
    if (match) select.value = match.value;
  }
  if (subjectInput && subjectText) subjectInput.value = subjectText;
  if (messageBox && subjectText && !messageBox.value.trim()) messageBox.value = subjectText;
  smoothScrollTo(".contact-form-section");
}

function filterStaticCompanyCards(keyword = "") {
  const cards = document.querySelectorAll(".companies-grid .company-card");
  if (!cards.length) return;
  const query = keyword.trim().toLowerCase();
  cards.forEach(card => {
    const text = card.textContent.toLowerCase();
    card.style.display = !query || text.includes(query) ? "" : "none";
  });
  smoothScrollTo(".companies-section");
}

function runHeroSearch() {
  const page = window.location.pathname;
  const input = document.querySelector(".hero .search-box input");
  const query = input?.value.trim() || "";
  const activeTab = getActiveHeroTabText();

  if (page.includes("index.html") || page === "/" || page.endsWith("/")) {
    searchHome();
    return;
  }

  if (page.includes("agents.html")) {
    if (activeTab === "Companies") {
      filterStaticCompanyCards(query);
    } else if (activeTab === "Appointments") {
      window.location.href = `appointments.html?type=${encodeURIComponent("Book Appointment")}${query ? `&agent=${encodeURIComponent(query)}` : ""}`;
    } else if (activeTab === "Reviews") {
      smoothScrollTo(".agents-trust-section");
    } else if (activeTab === "Locations") {
      query ? loadAgents({ location: query }) : smoothScrollTo(".agents-filters-section");
    } else {
      loadAgents(query ? { location: query } : {});
      smoothScrollTo(".agents-list-section");
    }
    return;
  }

  if (page.includes("properties.html")) {
    const tabTypeMap = {
      "Buy": "House",
      "Rent": "Rental",
      "New Listings": "New Listing",
      "Luxury": "Luxury",
      "Commercial": "Commercial"
    };
    const params = query ? { keyword: query } : (tabTypeMap[activeTab] ? { category: tabTypeMap[activeTab] } : {});
    loadProperties(params);
    smoothScrollTo(".featured-properties");
    return;
  }

  if (page.includes("appointments.html")) {
    setSupportRequestType(activeTab || "General Inquiry", query);
    return;
  }

  if (page.includes("about.html")) {
    const aboutMap = {
      "Our Story": ".about-story-section",
      "Mission": ".mission-section",
      "Vision": ".mission-section",
      "Team": ".team-section",
      "Support": ".about-cta"
    };
    smoothScrollTo(aboutMap[activeTab] || ".about-story-section");
    return;
  }

  if (page.includes("contact.html")) {
    setContactInquiryType(activeTab || "Contact", query);
  }
}

function handleHeroTabClick(tabText) {
  const page = window.location.pathname;

  if (page.includes("agents.html")) {
    const map = {
      "Agents": ".agents-list-section",
      "Companies": ".companies-section",
      "Appointments": ".agents-cta-section",
      "Reviews": ".agents-trust-section",
      "Locations": ".agents-filters-section"
    };
    if (tabText === "Agents") loadAgents();
    if (tabText === "Companies") filterStaticCompanyCards("");
    smoothScrollTo(map[tabText] || ".agents-list-section");
    return;
  }

  if (page.includes("properties.html")) {
    const map = {
      "Buy": "House",
      "Rent": "Rental",
      "New Listings": "New Listing",
      "Luxury": "Luxury",
      "Commercial": "Commercial"
    };
    if (map[tabText]) loadProperties({ category: map[tabText] });
    smoothScrollTo(".featured-properties");
    return;
  }

  if (page.includes("appointments.html")) {
    setSupportRequestType(tabText);
    return;
  }

  if (page.includes("about.html")) {
    const map = {
      "Our Story": ".about-story-section",
      "Mission": ".mission-section",
      "Vision": ".mission-section",
      "Team": ".team-section",
      "Support": ".about-cta"
    };
    smoothScrollTo(map[tabText] || ".about-story-section");
    return;
  }

  if (page.includes("contact.html")) {
    if (tabText === "FAQ") smoothScrollTo(".contact-faq-section");
    else if (tabText === "Office") smoothScrollTo(".contact-details-section");
    else if (tabText === "Support" || tabText === "Help") smoothScrollTo(".contact-support-section");
    else smoothScrollTo(".contact-form-section");
  }
}

function setupHeroActions() {
  const hero = document.querySelector(".hero");
  if (!hero || hero.dataset.heroActionsReady) return;
  hero.dataset.heroActionsReady = "true";

  hero.querySelectorAll(".tabs span").forEach(tab => {
    tab.style.cursor = "pointer";
    tab.addEventListener("click", () => {
      setHeroActiveTab(tab);
      handleHeroTabClick(tab.textContent.trim());
    });
  });

  const searchButton = hero.querySelector(".search-box button");
  const searchInput = hero.querySelector(".search-box input");
  if (searchButton) {
    searchButton.removeAttribute("onclick");
    searchButton.addEventListener("click", (e) => {
      e.preventDefault();
      runHeroSearch();
    });
  }
  if (searchInput) {
    searchInput.addEventListener("keydown", (e) => {
      if (e.key === "Enter") {
        e.preventDefault();
        runHeroSearch();
      }
    });
  }
}

document.addEventListener("DOMContentLoaded", function () {
  updateAuthUI(); setupAuthForm(); setupModalCloseHelpers(); setupHomeSmoothLinks(); setupAppointmentPageHelpers(); ensurePropertyViewingForm(); setupPropertyFilters(); setupHeroActions();
  const page = window.location.pathname;
  if (page.includes("agents.html")) { loadAgents(); loadCompanies(); }
  if (page.includes("properties.html")) { const params=new URLSearchParams(window.location.search); loadProperties(params.get("keyword") ? { keyword: params.get("keyword") } : {}); }
  const appointmentForm = document.querySelector(".support-form-grid:not(#propertyViewingForm)"); if (appointmentForm) appointmentForm.addEventListener("submit", submitAppointmentForm);
  const contactForm = document.querySelector(".contact-form-grid"); if (contactForm) contactForm.addEventListener("submit", submitContactForm);
  setupAdminPage(); revealOnScroll(); scrollToHashTarget();
});
