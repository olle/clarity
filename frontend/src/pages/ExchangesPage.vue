<template>
  <article class="clarity--content-page">
    <h1>Exchanges</h1>
    <p class="inline">
      Hide internal exchanges
      <small v-if="!!hide">({{ hidden }})</small>
      <ToggleSwitch v-model="hide" />
    </p>
    <ul style="display: none">
      <li v-for="exchange in decorated">
        {{ exchange.broker.name }}/<strong>{{ exchange.name }}</strong>
      </li>
    </ul>
    <Tree :value="nodes" :expandedKeys="{ [first]: true }">
      <template #default="slotProps">
        {{ slotProps.node.label }}
      </template>
      <template #exchange="slotProps">
        <div>
          <strong class="code"
            >&quot;{{ slotProps.node.label.name }}&quot;</strong
          >
          <Tag :value="slotProps.node.label.type" />
        </div>
      </template>
    </Tree>
  </article>
</template>

<script setup>
import { onMounted, computed, ref } from "vue";
import { storeToRefs } from "pinia";
import { useExchangeStore } from "../stores/useExchangeStore";
import { useBrokerStore } from "../stores/useBrokerStore";
import ToggleSwitch from "primevue/toggleswitch";
import Tree from "primevue/tree";
import Tag from "primevue/tag";

const store = useExchangeStore();
const { exchanges } = storeToRefs(store);
const brokerStore = useBrokerStore();
const { brokers } = storeToRefs(brokerStore);

const hide = ref(true);

const filtered = computed(() =>
  Object.values(exchanges.value).filter((exchange) =>
    !!hide.value
      ? exchange.attributes.userWhoPerformedAction !== "rmq-internal"
      : true
  )
);

const hidden = computed(
  () => Object.values(exchanges.value).length - filtered.value.length
);

const decorated = computed(() =>
  filtered.value.map(brokerStore.decorateExchange)
);

const nodes = computed(() => {
  const brokerNodes = Object.values(brokers.value).map((broker) => {
    const brokerExchanges = decorated.value.filter(
      (exchange) => exchange.broker.id === broker.id
    );
    return {
      type: "broker",
      key: broker.id,
      label: broker.name,
      children: brokerExchanges.map((exchange) => ({
        type: "exchange",
        key: exchange.id,
        label: exchange,
      })),
    };
  });
  return brokerNodes;
});

const first = computed(() => nodes.value.map((node) => node.key)[0]);

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

.code {
  font-family: monospace;
}
</style>
