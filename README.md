## My Files

<div>
Tecnologias utilizadas:
<code><span>SpringBoot</span><img height="14" src="https://dz2cdn1.dzone.com/storage/temp/12434118-spring-boot-logo.png" align="center"/></code>
<code><span>Docker</span><img height="14"src="https://seeklogo.com/images/D/docker-logo-CF97D0124B-seeklogo.com.png" align="center"/></code>
</div>

### Nota:
Esta aplicación esta siendo ejecutada en un contenedor de docker en un host gratuito, al tener un tiempo de inactividad la aplicacion entra en un estado de sueño por lo que es probablemente haya retardo en la respuesta al momento de ingresar.

### ¿Que es My Files?
App en donde los usuarios pueden almacenar archivos basado en una suscripcion.

### Motivo
Esta aplicacion fue desarrollada con el fin de practicar mis habilidades en el backend tanto con Springboot como con NestJs, y conocer tanto sus similitudes como sus diferencias.

### Caracteristicas
- [x] Almacenamiento de archivos en AWS S3.
- [x] Carga de avatar de usuario en Cloudinary.
- [x] Validacion de contenido de avatars utilizando AWS Rekognition.
- [x] Despliegue de aplicacion dockerizada.
- [ ] Metodo de autenticacion con JWT.
- [ ] Inicio de sesion con Google.
- [ ] Notificacion de email.
- [ ] Metodo de verificacion de 2 pasos con Google Authenticator.
- [ ] Metodo de suscripcion a un plan de pago utilizando Stripe.
- [ ] Busqueda de datos utilizando ElasticSearch.
- [ ] Automatizacion con CI de la imagen de docker construida.
- [ ] Documentacion de endpoints con Swagger.

### Uso
Este proyecto es totalmente libre, para ejecutarlo realiza un clone de este repositorio y agrega tu API KEY en la siguiente variable de entorno, despues elimina la extension ".template" del nombre del archivo.
```ts
// docker.env.template
# CLOUDINARY
CLOUDINARY_CLOUD_NAME=
CLOUDINARY_API_KEY=
CLOUDINARY_API_SECRET=

# AWS
AWS_ACCESS_KEY=
AWS_SECRET_KEY=
AWS_REGION=
AWS_BUCKET_NAME=
AWS_BUCKET_NAME_PRIVATE=
```

Para una mayor facilidad se recomienda uso de docker, situados en la terminal junto al archivo docker-compose.yml ejecute el siguiente comando:
```ts
docker compose up
```

La aplicación correra en un puerto 8080 por defecto, si encuentra algun problema al momento de la ejecución cambie el puerto a uno libre, o termine la tarea que se este ejecutando.
