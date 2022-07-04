const userAdminRouter = require("./admin/routes");

const express = require('express')
const app = express()
const port = 3500

app.get('/', (req, res) => {
  res.send('Hello World!')
})


app.use("/api/v1/user", userAdminRouter);


app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
})