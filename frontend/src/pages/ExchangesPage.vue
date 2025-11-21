<template>
  <article class="clarity--content-page">
    <h1>Exchanges</h1>
    <p class="inline">
      Hide internal exchanges
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
  margin-left: .5rem;
}

ul {
  margin: 0;
  padding: 0;
  list-style: none;
}
</style>
