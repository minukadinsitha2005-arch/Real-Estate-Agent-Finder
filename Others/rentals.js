/* ============================================================
   RENTALS.JS – Real Estate Agent Finder | Rentals Page
============================================================ */

/* ---- Mobile Nav Toggle ---- */
const navToggle = document.getElementById('navToggle');
const navLinks  = document.getElementById('navLinks');

navToggle.addEventListener('click', () => {
  navLinks.classList.toggle('open');
  const bars = navToggle.querySelectorAll('span');
  const isOpen = navLinks.classList.contains('open');
  bars[0].style.transform = isOpen ? 'translateY(7px) rotate(45deg)' : '';
  bars[1].style.opacity   = isOpen ? '0' : '1';
  bars[2].style.transform = isOpen ? 'translateY(-7px) rotate(-45deg)' : '';
});

/* Close nav on outside click */
document.addEventListener('click', (e) => {
  if (!navToggle.contains(e.target) && !navLinks.contains(e.target)) {
    navLinks.classList.remove('open');
    navToggle.querySelectorAll('span').forEach(s => {
      s.style.transform = '';
      s.style.opacity   = '1';
    });
  }
});

/* ---- Favourite / Save Button Toggle ---- */
document.querySelectorAll('.btn-fav').forEach(btn => {
  btn.addEventListener('click', () => {
    btn.classList.toggle('saved');
    const icon = btn.querySelector('i');
    if (btn.classList.contains('saved')) {
      icon.classList.replace('far', 'fas');
      btn.title = 'Saved';
    } else {
      icon.classList.replace('fas', 'far');
      btn.title = 'Save';
    }
  });
});

/* ---- Filters ---- */
function applyFilters() {
  const btn = document.querySelector('.btn-apply');
  const original = btn.innerHTML;
  btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Applying…';
  btn.disabled = true;

  setTimeout(() => {
    btn.innerHTML = '<i class="fas fa-check"></i> Applied';
    btn.style.background = 'linear-gradient(135deg, #27AE60, #1E8449)';
    setTimeout(() => {
      btn.innerHTML = original;
      btn.style.background = '';
      btn.disabled = false;
    }, 1800);
  }, 700);
}

function clearFilters() {
  ['f-location','f-price','f-type','f-beds','f-baths','f-furnished'].forEach(id => {
    const el = document.getElementById(id);
    if (el) el.selectedIndex = 0;
  });
  document.getElementById('resultCount').textContent = 'Showing 9 properties';
}

/* ---- Sort ---- */
function sortCards() {
  const grid  = document.getElementById('propertyGrid');
  const cards = Array.from(grid.querySelectorAll('.property-card'));
  const sortBy = document.getElementById('sortSelect').value;

  cards.forEach(c => c.style.opacity = '0');

  setTimeout(() => {
    if (sortBy === 'Price: Low to High') {
      cards.sort((a, b) => getPrice(a) - getPrice(b));
    } else if (sortBy === 'Price: High to Low') {
      cards.sort((a, b) => getPrice(b) - getPrice(a));
    }
    // For 'Newest' and 'Most Popular' keep original order (already in DOM order)
    cards.forEach(c => { grid.appendChild(c); c.style.opacity = '1'; });
  }, 250);
}

function getPrice(card) {
  const priceText = card.querySelector('.card-price')?.textContent || '0';
  return parseInt(priceText.replace(/[^\d]/g, ''), 10) || 0;
}

/* ---- Load More ---- */
const extraCards = [
  {
    img: 'https://images.unsplash.com/photo-1600047509807-ba8f99d2cdde?w=600&q=80',
    type: 'House',
    price: 'LKR 92,000',
    title: 'Charming 3-Bed Home – Rajagiriya',
    location: 'Rajagiriya, Colombo',
    beds: '3', baths: '2', furnished: 'Unfurnished',
    desc: 'Quiet residential area with excellent schools nearby. Spacious garden and two-car garage.'
  },
  {
    img: 'https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?w=600&q=80',
    type: 'Apartment',
    price: 'LKR 58,000',
    title: 'Bright 2-Bed Apartment – Wellawatte',
    location: 'Wellawatte, Colombo',
    beds: '2', baths: '1', furnished: 'Semi-Furnished',
    desc: 'Walk to the beach from this airy apartment with sea-breeze balcony and modern kitchen.'
  },
  {
    img: 'https://images.unsplash.com/photo-1572120360610-d971b9d7767c?w=600&q=80',
    type: 'Villa',
    price: 'LKR 175,000',
    title: 'Garden Villa – Dehiwala',
    location: 'Dehiwala, Colombo',
    beds: '4', baths: '3', furnished: 'Furnished',
    desc: 'Tropical garden villa with private pool, guest suite, and smart home automation system.'
  }
];

let loaded = false;

function loadMore() {
  if (loaded) return;
  loaded = true;

  const grid = document.getElementById('propertyGrid');
  const btn  = document.querySelector('.btn-loadmore');
  btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Loading…';
  btn.disabled = true;

  setTimeout(() => {
    extraCards.forEach((data, i) => {
      const card = document.createElement('div');
      card.className = 'property-card';
      card.style.opacity = '0';
      card.style.transform = 'translateY(20px)';
      card.style.transition = 'opacity .4s ease, transform .4s ease';
      card.innerHTML = `
        <div class="card-img-wrap">
          <img src="${data.img}" alt="${data.title}" loading="lazy"/>
          <span class="badge-type">${data.type}</span>
          <button class="btn-fav" title="Save"><i class="far fa-heart"></i></button>
        </div>
        <div class="card-body">
          <div class="card-price">${data.price} <span>/month</span></div>
          <h3 class="card-title">${data.title}</h3>
          <p class="card-location"><i class="fas fa-map-marker-alt"></i> ${data.location}</p>
          <div class="card-meta">
            <span><i class="fas fa-bed"></i> ${data.beds} Beds</span>
            <span><i class="fas fa-bath"></i> ${data.baths} Baths</span>
            <span><i class="fas fa-couch"></i> ${data.furnished}</span>
          </div>
          <p class="card-desc">${data.desc}</p>
          <a href="#" class="btn-view">View Details <i class="fas fa-chevron-right"></i></a>
        </div>`;

      grid.appendChild(card);

      // Stagger fade-in
      setTimeout(() => {
        card.style.opacity = '1';
        card.style.transform = 'translateY(0)';
      }, i * 120);

      // Re-attach fav listener
      card.querySelector('.btn-fav').addEventListener('click', function() {
        this.classList.toggle('saved');
        const icon = this.querySelector('i');
        icon.classList.toggle('far', !this.classList.contains('saved'));
        icon.classList.toggle('fas', this.classList.contains('saved'));
        this.title = this.classList.contains('saved') ? 'Saved' : 'Save';
      });
    });

    btn.innerHTML = '<i class="fas fa-check-circle"></i> All Properties Loaded';
    btn.style.borderColor = 'var(--green)';
    btn.style.color = 'var(--green-dark)';

    // Update count
    document.getElementById('resultCount').textContent = `Showing ${9 + extraCards.length} properties`;
  }, 900);
}

/* ---- Navbar shadow on scroll ---- */
const navbar = document.querySelector('.navbar');
window.addEventListener('scroll', () => {
  if (window.scrollY > 10) {
    navbar.style.boxShadow = '0 4px 30px rgba(0,0,0,.14)';
  } else {
    navbar.style.boxShadow = '0 2px 20px rgba(0,0,0,.08)';
  }
}, { passive: true });

/* ---- Scroll-reveal cards ---- */
const observer = new IntersectionObserver((entries) => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
      entry.target.style.opacity = '1';
      entry.target.style.transform = 'translateY(0)';
      observer.unobserve(entry.target);
    }
  });
}, { threshold: 0.1 });

document.querySelectorAll('.property-card').forEach((card, i) => {
  card.style.opacity = '0';
  card.style.transform = 'translateY(24px)';
  card.style.transition = `opacity .45s ease ${i * 80}ms, transform .45s ease ${i * 80}ms`;
  observer.observe(card);
});
