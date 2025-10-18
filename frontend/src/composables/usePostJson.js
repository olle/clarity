export function usePostJson() {
  async function post(url, value) {
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

  return { post };
}
