// Shows a short message from the URL, e.g. /posts?msg=Post+created
document.addEventListener('DOMContentLoaded', () => {
  const msg = new URLSearchParams(window.location.search).get('msg');
  const flash = document.getElementById('flash');
  if (msg && flash) {
    flash.textContent = msg;   // textContent shows it as plain text, never as HTML
  }
});