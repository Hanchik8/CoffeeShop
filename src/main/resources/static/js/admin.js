function getCsrfMeta() {
  var tokenMeta = document.querySelector('meta[name="_csrf"]');
  var headerMeta = document.querySelector('meta[name="_csrf_header"]');
  return {
    token: tokenMeta ? tokenMeta.getAttribute("content") : null,
    header: headerMeta ? headerMeta.getAttribute("content") : null
  };
}

function getCookie(name) {
  var value = "; " + document.cookie;
  var parts = value.split("; " + name + "=");
  if (parts.length === 2) {
    return parts.pop().split(";").shift();
  }
  return null;
}

function getCsrf() {
  var meta = getCsrfMeta();
  if (meta.token && meta.header) {
    return meta;
  }
  var token = getCookie("XSRF-TOKEN");
  if (token) {
    return { token: token, header: "X-XSRF-TOKEN" };
  }
  return { token: null, header: null };
}

async function fetchWithCsrf(url, opts) {
  var options = opts || {};
  var headers = new Headers(options.headers || {});
  var csrf = getCsrf();
  if (csrf.token && csrf.header) {
    headers.set(csrf.header, csrf.token);
  }
  var finalOpts = Object.assign({}, options, { headers: headers, credentials: "same-origin" });
  var resp = await fetch(url, finalOpts);
  if (!resp.ok) {
    var body;
    try {
      body = await resp.json();
    } catch (e) {
      body = await resp.text();
    }
    var err = new Error("HTTP " + resp.status);
    err.status = resp.status;
    err.body = body;
    throw err;
  }
  return resp;
}

function formatError(err) {
  if (!err) {
    return "Unexpected error";
  }
  if (err.body) {
    if (typeof err.body === "string") {
      return err.body;
    }
    if (typeof err.body === "object") {
      var msg = err.body.message || "Request failed";
      var details = err.body.details;
      if (details && typeof details === "object") {
        var parts = [];
        Object.keys(details).forEach(function(key) {
          parts.push(key + ": " + details[key]);
        });
        if (parts.length > 0) {
          return msg + " (" + parts.join(", ") + ")";
        }
      }
      return msg;
    }
  }
  return err.message || "Request failed";
}

function setupAdminPage() {
  var errorEl = document.getElementById("adminError");
  var showError = function(message) {
    if (!errorEl) return;
    errorEl.textContent = message;
    errorEl.classList.add("show");
  };
  var clearError = function() {
    if (!errorEl) return;
    errorEl.textContent = "";
    errorEl.classList.remove("show");
  };

  var categoryForm = document.querySelector('[data-admin-form="category"]');
  if (categoryForm) {
    categoryForm.addEventListener("submit", function(e) {
      e.preventDefault();
      clearError();
      var fd = new FormData(categoryForm);
      fetchWithCsrf("/api/admin/category", { method: "POST", body: fd })
        .then(function() {
          window.location.reload();
        })
        .catch(function(err) {
          showError(formatError(err));
        });
    });

    var categoryList = document.getElementById("categoryList");
    if (categoryList) {
      categoryList.addEventListener("click", function(ev) {
        var btn = ev.target.closest('[data-action="delete-category"]');
        if (!btn) return;
        ev.preventDefault();
        clearError();
        var id = btn.getAttribute("data-id");
        if (!window.confirm("Delete category " + id + "?")) return;
        fetchWithCsrf("/api/admin/category/" + id, { method: "DELETE" })
          .then(function() {
            window.location.reload();
          })
          .catch(function(err) {
            showError(formatError(err));
          });
      });
    }
  }

  var itemForm = document.querySelector('[data-admin-form="item"]');
  if (itemForm) {
    itemForm.addEventListener("submit", function(e) {
      e.preventDefault();
      clearError();
      var fd = new FormData(itemForm);
      fetchWithCsrf("/api/admin/item", { method: "POST", body: fd })
        .then(function() {
          window.location.reload();
        })
        .catch(function(err) {
          showError(formatError(err));
        });
    });

    var itemList = document.getElementById("itemList");
    if (itemList) {
      itemList.addEventListener("click", function(ev) {
        var btn = ev.target.closest('[data-action="delete-item"]');
        if (!btn) return;
        ev.preventDefault();
        clearError();
        var id = btn.getAttribute("data-id");
        if (!window.confirm("Delete item " + id + "?")) return;
        fetchWithCsrf("/api/admin/item/" + id, { method: "DELETE" })
          .then(function() {
            window.location.reload();
          })
          .catch(function(err) {
            showError(formatError(err));
          });
      });
    }
  }
}

document.addEventListener("DOMContentLoaded", setupAdminPage);
