// Shows a short message from the URL, e.g. /posts?msg=Post+created
document.addEventListener('DOMContentLoaded', () => {
  const msg = new URLSearchParams(window.location.search).get('msg');
  const flash = document.getElementById('flash');
  if (msg && flash) {
    flash.textContent = msg;   // Fix for Vulnerability 4: textContent inserts plain text, so the browser never parses it as HTML
  }
});