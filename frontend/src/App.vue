<template>
  <div class="wrapper">
    <nav></nav>
    <article>
      <h1>Welcome to <em class="highlight">Clarity!</em></h1>
    </article>
  </div>
</template>

<script setup></script>

<style scoped>
h1 {
  font-size: 3.2em;
  line-height: 1.1;
}

.wrapper {
  border-top: 1px solid #eee;
  height: 100%;
  --width: 61px;
  display: grid;
  grid-template-columns: var(--width) calc(100vw - var(--width));
  background: #fcfcfb;
}

header {
  border-bottom: 1px solid #eee;
}

nav {
  background-color: white;
  border-right: 1px solid #eee;
  box-shadow: 0px 2px 9px rgba(0, 0, 0, 0.1);
}

article {
  align-self: center;
  justify-content: center;
  align-items: center;
  align-content: center;
  text-align: center;
}

.highlight {
  color: #15bb8c;
}
</style>
