export function useApi() {
  async function fetchJSON(url) {
    try {
      const response = await fetch(url);
      if (!response.ok) {
        return false;
      }
      const result = await response.json();
      return result;
    } catch (error) {
      return false;
    }
  }

  async function postJSON(url, value) {
    try {
      const response = await fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(value),
      });
      return response.ok;
    } catch (error) {
      return false;
    }
  }

  async function deleteRequest(url) {
    try {
      const response = await fetch(url, {
        method: "DELETE",
      });
      return response.ok;
    } catch (error) {
      return false;
    }
  }

  const brokers = {
    create: async (broker) => postJSON("/api/v0/brokers", broker),
    fetchAll: async () => fetchJSON("/api/v0/brokers"),
    delete: async (broker) => deleteRequest(`/api/v0/brokers/${broker.id}`),
  };

  return { brokers };
}
