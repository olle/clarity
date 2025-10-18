<template>
  <article>
    <Toast />
    <h2>Brokers</h2>

    <ul>
      <li v-for="broker in store.brokers">
        {{ broker }}
        <Button
          label="Delete"
          severity="danger"
          size="small"
          variant="outlined"
          @click="store.removeBroker(broker)"
        />
      </li>
    </ul>

    <Panel header="Add new broker" class="add-broker-panel">
      <Form
        v-slot="$form"
        :initialValues
        :resolver
        @submit="onFormSubmit"
        class="add-broker-form"
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
          <Password name="password" inputId="password" fluid />
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
            <Checkbox inputId="ssl" name="ssl" value="true" />
            SSL
          </label>
        </div>
        <div></div>
        <Button type="submit" severity="secondary" label="Submit" />
      </Form>
    </Panel>
  </article>
</template>

<script setup>
import { reactive, onMounted } from "vue";

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

const initialValues = reactive({
  host: "localhost",
  port: 5672,
  username: "guest",
  password: "guest",
  ssl: false,
});

const resolver = valibotResolver(
  v.object({
    host: v.pipe(v.string(), v.minLength(1)),
    port: v.pipe(v.number(), v.minValue(1), v.maxValue(65535)),
    username: v.pipe(v.string(), v.minLength(1)),
    password: v.pipe(v.string(), v.minLength(1)),
  })
);

const onFormSubmit = ({ valid, values }) => {
  if (valid) {
    toast.add({
      severity: "success",
      summary: "Form is submitted.",
      life: 3000,
    });
    store.addBroker(values);
  }
};
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
