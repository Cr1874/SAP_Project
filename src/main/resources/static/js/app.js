// Shows a short message from the URL, e.g. /posts?msg=Post+created
document.addEventListener('DOMContentLoaded', () => {
  const msg = new URLSearchParams(window.location.search).get('msg');
  const flash = document.getElementById('flash');
  if (msg && flash) {
    flash.innerHTML = msg;   // Vulnerability 4: msg from the URL is parsed as HTML in the browser
  }
});