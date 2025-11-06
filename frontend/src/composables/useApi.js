import { useCSRF } from "./useCSRF";

export function useApi() {
  const { csrfHeader } = useCSRF();

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

  async function postJsonRequest(url, value) {
    try {
      const response = await fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json", ...csrfHeader.value },
        body: JSON.stringify(value),
      });
      return response.ok;
    } catch (error) {
      return false;
    }
  }

  async function putJsonRequest(url, value) {
    try {
      const response = await fetch(url, {
        method: "PUT",
        headers: { "Content-Type": "application/json", ...csrfHeader.value },
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
        headers: { ...csrfHeader.value },
      });
      return response.ok;
    } catch (error) {
      return false;
    }
  }

  const brokers = {
    create: async (broker) => postJsonRequest("/api/v0/brokers", broker),
    update: async (id, updated) =>
      putJsonRequest(`/api/v0/brokers/${id}`, updated),
    delete: async (broker) => deleteRequest(`/api/v0/brokers/${broker.id}`),
    fetchAll: async () => fetchJSON("/api/v0/brokers"),
    activate: async (broker) =>
      postJsonRequest(`/api/v0/brokers/${broker.id}/activate`, broker),
    deactivate: async (broker) =>
      postJsonRequest(`/api/v0/brokers/${broker.id}/deactivate`, broker),
  };

  const exchanges = {
    fetchAll: async () => fetchJSON("/api/v0/exchanges"),
  };

  return { brokers, exchanges };
}
