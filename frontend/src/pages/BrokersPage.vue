<template>
  <article>
    <Toast />
    <h2>RabbitMQ Brokers</h2>

    <ul>
      <li v-for="broker in store.brokers">
        {{ broker }}
        <Button
          label="Edit"
          severity="secondary"
          size="small"
          variant="outlined"
          @click="editBroker(broker)"
        />
        <Button
          label="Delete"
          severity="danger"
          size="small"
          variant="outlined"
          @click="removeBroker(broker)"
        />
      </li>
    </ul>

    <Panel header="Add new broker" class="add-broker-panel">
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

const initialValues = ref({ ...initial });

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
  }
};

function resetForm() {
  formResetKey.value++;
  initialValues.value = { ...initial };
}

const editBrokerId = ref(null);
function editBroker(broker) {
  formResetKey.value++;
  editBrokerId.value = broker.id;
  initialValues.value = broker;
}

function removeBroker(broker) {
  store.removeBroker(broker);
}
</script>

<style scoped>
article {
  padding: 0 1rem 1rem 1rem;
}

label:not(.checkbox-label) {
  display: block;
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
</style>
