<template>
  <article class="clarity--content-page">
    <Toast />
    <div class="header">
      <div class="col">
        <h1>RabbitMQ Brokers</h1>
      </div>
      <div class="col ms-auto">
        <Button label="Add RabbitMQ broker" @click="addNewBroker()">
          <template #icon>
            <IconPlus />
          </template>
        </Button>
      </div>
    </div>

    <Panel :header="formLabel" class="add-broker-panel" v-if="showForm">
      <template #icons>
        <Button
          severity="danger"
          rounded
          aria-label="Close"
          variant="outlined"
          size="small"
          class="ms-auto"
          @click="closeForm()"
        >
          <template #icon>
            <IconX />
          </template>
        </Button>
      </template>
      <Form
        v-slot="$form"
        :key="formResetKey"
        :initialValues
        :resolver
        @submit="onFormSubmit"
        class="add-broker-form"
        autocomplete="off"
      >
        <div>
          <label for="host">Hostname</label>
          <InputText name="host" id="host" fluid />
          <Message
            v-if="$form.host?.invalid"
            severity="error"
            size="small"
            variant="simple"
            >{{ $form.host.error?.message }}</Message
          >
        </div>
        <div>
          <label for="port">Port</label>
          <InputNumber name="port" inputId="port" :useGrouping="false" fluid />
          <Message
            v-if="$form.port?.invalid"
            severity="error"
            size="small"
            variant="simple"
            >{{ $form.port.error?.message }}</Message
          >
        </div>
        <div>
          <label for="username">Username</label>
          <InputText name="username" id="username" autocomplete="off" fluid />
          <Message
            v-if="$form.username?.invalid"
            severity="error"
            size="small"
            variant="simple"
            >{{ $form.username.error?.message }}</Message
          >
        </div>
        <div>
          <label for="password">Password</label>
          <Password
            name="password"
            inputId="password"
            fluid
            toggleMask
            autocomplete="off"
          />
          <Message
            v-if="$form.password?.invalid"
            severity="error"
            size="small"
            variant="simple"
            >{{ $form.password.error?.message }}</Message
          >
        </div>
        <div>
          <label for="ssl" class="checkbox-label">
            <Checkbox v-model="ssl" inputId="ssl" name="ssl" binary />
            SSL
          </label>
        </div>
        <div></div>
        <Button type="submit" severity="secondary" label="Submit" />
        <Button
          type="reset"
          severity="warn"
          label="Reset"
          outlined
          @click="resetForm"
        />
      </Form>
    </Panel>

    <ul class="broker-list">
      <li v-for="broker in store.brokers" :key="broker.id">
        <dl class="broker-details">
          <dt>Host:</dt>
          <dd>{{ broker.host }}</dd>
          <dt>Port:</dt>
          <dd>{{ broker.port }}</dd>
          <dt>Type:</dt>
          <dd>{{ broker.type }}</dd>
          <dt>URL:</dt>
          <dd>
            amqp://{{ broker.username }}:*******@{{ broker.host }}:{{
              broker.port
            }}
          </dd>
        </dl>
        <div>
          <Button
            :disabled="broker.type === 'CONFIGURED'"
            label="Edit"
            severity="secondary"
            size="small"
            variant="outlined"
            @click="editBroker(broker)"
          />
          <Button
            :disabled="broker.type === 'CONFIGURED'"
            label="Delete"
            severity="danger"
            size="small"
            variant="outlined"
            @click="removeBroker(broker)"
          />
        </div>
      </li>
    </ul>
  </article>
</template>

<script setup>
import { ref, onMounted, nextTick } from "vue";

import Panel from "primevue/panel";
import { Form } from "@primevue/forms";
import InputText from "primevue/inputtext";
import Button from "primevue/button";
import Message from "primevue/message";
import InputNumber from "primevue/inputnumber";
import Password from "primevue/password";
import Checkbox from "primevue/checkbox";
import Toast from "primevue/toast";

import { valibotResolver } from "@primevue/forms/resolvers/valibot";
import * as v from "valibot";
import { useBrokerStore } from "../composables/useBrokerStore";
import { useToast } from "primevue/usetoast";
import { IconPlus, IconX } from "@tabler/icons-vue";

const toast = useToast();
const store = useBrokerStore();

onMounted(() => {
  store.reload();
});

const formResetKey = ref(0);

const initial = {
  host: "",
  port: 5672,
  username: "",
  password: "",
  ssl: false,
};

const formLabel = ref("Add new broker");
const showForm = ref(false);
const initialValues = ref(null);

const resolver = valibotResolver(
  v.object({
    host: v.pipe(v.string(), v.minLength(1)),
    port: v.pipe(v.number(), v.minValue(1), v.maxValue(65535)),
    username: v.pipe(v.string(), v.minLength(1)),
    password: v.pipe(v.string(), v.minLength(1)),
    ssl: v.boolean(),
  })
);

const onFormSubmit = ({ valid, values, reset }) => {
  if (valid) {
    toast.add({
      severity: "success",
      summary: "Form is submitted.",
      life: 3000,
    });

    if (editBrokerId.value !== null) {
      const id = editBrokerId.value;
      editBrokerId.value = null;
      store.updateBroker(id, values);
    } else {
      store.addBroker(values);
    }

    nextTick();
    resetForm();
    showForm.value = false;
  }
};

function closeForm() {
  resetForm();
  showForm.value = false;
}

function resetForm() {
  formResetKey.value++;
  initialValues.value = { ...initial };
}

function addNewBroker() {
  formLabel.value = "Add new broker";
  formResetKey.value++;
  editBrokerId.value = null;
  initialValues.value = { ...initial };
  showForm.value = true;
}

const editBrokerId = ref(null);
function editBroker(broker) {
  formResetKey.value++;
  editBrokerId.value = broker.id;
  initialValues.value = broker;
  formLabel.value = "Edit broker";
  showForm.value = true;
}

function removeBroker(broker) {
  store.removeBroker(broker);
  resetForm();
  showForm.value = false;
}
</script>

<style scoped>
.header {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 2rem;
}

.header h1 {
  padding: 0;
  margin: 0;
}

label:not(.checkbox-label) {
  display: block;
}

.broker-list {
  list-style: none;
  padding: 0;
}
.broker-list li {
  padding: 0.5rem;
  border-bottom: 1px solid #eee;
  display: flex;
  align-items: center;
  justify-content: space-between;
  justify-items: baseline;
  gap: 1rem;
}

.broker-list li > * {
  margin-top: auto;
}

.broker-details {
  display: grid;
  grid-template-columns: max-content auto;
  gap: 0.5rem;
  align-items: first baseline;
}
.broker-details dt {
  font-weight: 600;
}
.broker-details dd {
  margin: 0;
}

.add-broker-panel {
  max-width: 600px;
  padding: 1rem;
}
.add-broker-form {
  display: grid;
  gap: 1rem;
  grid-template-columns: 1fr 1fr;
}

.ms-auto {
  margin-left: auto;
}
</style>
