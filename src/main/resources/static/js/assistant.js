// TechKart assistant chat panel. Plain JavaScript, no framework.
(function () {
  const csrfMeta = document.querySelector('meta[name="_csrf"]');
  const headerMeta = document.querySelector('meta[name="_csrf_header"]');
  if (!csrfMeta || !headerMeta) {
    return;
  }
  const STORE_KEY = 'techkart-assistant-log';

  const box = document.createElement('div');
  box.id = 'assistant';
  box.innerHTML =
    '<div id="assistant-head">TechKart assistant</div>' +
    '<div id="assistant-log"></div>' +
    '<form id="assistant-form"><input id="assistant-input" maxlength="1000" ' +
    'placeholder="Ask about products or your orders..." autocomplete="off"><button>Send</button></form>';
  document.body.appendChild(box);

  const log = box.querySelector('#assistant-log');
  const form = box.querySelector('#assistant-form');
  const input = box.querySelector('#assistant-input');
  let history = [];

  function save() {
    try {
      sessionStorage.setItem(STORE_KEY, JSON.stringify(history.slice(-30)));
    } catch (e) {
      // Storage can be unavailable; the chat still works without it.
    }
  }

  // textContent, never innerHTML: model output and store data are untrusted text.
  function render(text, who) {
    const row = document.createElement('div');
    row.className = 'assistant-msg ' + who;
    row.textContent = text;
    log.appendChild(row);
    log.scrollTop = log.scrollHeight;
    return row;
  }

  function add(text, who) {
    history.push({ text: text, who: who });
    save();
    return render(text, who);
  }

  async function post(url, body) {
    const response = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', [headerMeta.content]: csrfMeta.content },
      body: body ? JSON.stringify(body) : null
    });
    if (!response.ok) {
      throw new Error('Request failed: ' + response.status);
    }
    return response.json();
  }

  function addConfirm(pending) {
    const row = render('Cancel your order for ' + pending.productName + ' (₹' + pending.total + ')? ', 'bot');
    const button = document.createElement('button');
    button.textContent = 'Confirm';
    button.onclick = async function () {
      button.disabled = true;
      try {
        const result = await post('/assistant/confirm/' + pending.id);
        add(result.reply, 'bot');
        // Show the new status if the customer is looking at their orders.
        if (location.pathname.indexOf('/orders') === 0) {
          setTimeout(function () { location.reload(); }, 1200);
        }
      } catch (e) {
        add('Could not confirm. Please try again.', 'bot');
      }
    };
    row.appendChild(button);
  }

  try {
    history = JSON.parse(sessionStorage.getItem(STORE_KEY) || '[]');
  } catch (e) {
    history = [];
  }
  history.forEach(function (m) { render(m.text, m.who); });

  form.addEventListener('submit', async function (event) {
    event.preventDefault();
    const message = input.value.trim();
    if (!message) {
      return;
    }
    add(message, 'user');
    input.value = '';
    const waiting = render('...', 'bot');
    try {
      const result = await post('/assistant/chat', { message: message });
      waiting.remove();
      add(result.reply, 'bot');
      if (result.pendingAction) {
        addConfirm(result.pendingAction);
      }
    } catch (e) {
      waiting.textContent = 'Something went wrong. Please try again.';
    }
  });
})();
