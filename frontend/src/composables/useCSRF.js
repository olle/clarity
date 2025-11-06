import { onMounted, ref, computed, watch } from "vue";

/**
 * NOTE: This expects the CSRF token to be provided as a cookie called
 * 'XSRF-TOKEN'. See https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-javascript-spa
 * for more details.
 */
export function useCSRF() {
  const csrfToken = ref(null);

  onMounted(() => {
    const match = document.cookie.match(new RegExp("(^| )XSRF-TOKEN=([^;]+)"));
    if (match) {
      csrfToken.value = decodeURIComponent(match[2]);
    }
  });

  const csrfHeader = computed(() => {
    return csrfToken.value ? { "X-XSRF-TOKEN": csrfToken.value } : {};
  });

  watch(csrfToken, (newToken) => {
    if (newToken) {
      console.log("CSRF token set:", newToken);
    } else {
      console.log("CSRF token not found.");
    }
  });

  return {
    csrfToken,
    csrfHeader,
  };
}
