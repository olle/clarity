<template>
  <article class="clarity--content-page">
    <h1>Exchanges</h1>
    <p class="inline">
      Hide internal exchanges
      <small v-if="!!hide">({{ hidden }})</small>
      <ToggleSwitch v-model="hide" />
    </p>
    <ul>
      <li v-for="exchange in decorated">
        {{ exchange.broker.name }}/<strong>{{ exchange.name }}</strong>
      </li>
    </ul>
  </article>
</template>

<script setup>
import { onMounted, computed, ref } from "vue";
import { storeToRefs } from "pinia";
import { useExchangeStore } from "../stores/useExchangeStore";
import { useBrokerStore } from "../stores/useBrokerStore";
import ToggleSwitch from "primevue/toggleswitch";

const store = useExchangeStore();
const { exchanges } = storeToRefs(store);
const brokerStore = useBrokerStore();

const hide = ref(true);

const hidden = computed(
  () =>
    Object.values(exchanges.value).filter(
      (exchange) =>
        exchange.attributes.userWhoPerformedAction === "rmq-internal"
    ).length
);

const decorated = computed(() =>
  Object.values(exchanges.value)
    .filter((exchange) =>
      !!hide.value
        ? exchange.attributes.userWhoPerformedAction !== "rmq-internal"
        : true
    )
    .map(brokerStore.decorateExchange)
);

onMounted(() => {
  store.reload();
});
</script>

<style lang="css" scoped>
.inline {
  display: flex;
  align-items: center;
}
.inline > * {
  margin-left: 0.5rem;
}
.inline > small {
  font-weight: bold;
  margin-left: 0.2rem;
}

ul {
  margin: 0;
  padding: 0;
  list-style: none;
}
</style>
