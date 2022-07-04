import express from 'express';
import swaggerUi from 'swagger-ui-express';
import swaggerJsdoc from 'swagger-jsdoc';


import {AdminRouter} from './admin/routes.js'

class Server {
  constructor(port) {
		this.port = port;
    this.app = express(); 
    this.app.use(express.json())
    this.app.use(express.urlencoded({extended: true}))
    this.configureRouters();
	}

  configureRouters(){
    const adminRouter = new AdminRouter();
    adminRouter.configurePaths();
    this.app.use("/api/v1/user", adminRouter.getRouter());

    this.app.get('/', (req, res) => {
      res.send('Hello World!')
    })


    const options = {
      definition: {
        openapi: '3.0.0',
        info: {
          title: 'Hello World',
          version: '1.0.0',
        },
      },
      apis: ['./admin/routes.js'], // files containing annotations as above
    };
    
    const swaggerSpec = swaggerJsdoc(options);

    this.app.use('/api-docs', swaggerUi.serve, swaggerUi.setup(swaggerSpec));
  }

  start(){
    this.app.listen(this.port, () => {
      console.log(`Example app listening on port ${this.port}`)
    })
  }

}

let server = new Server(3500);
server.start();
