# FileBrowser By AndersonB.
## [Ver Frontend](https://github.com/DAndersonBurga/filebrowser-frontend.git)

## Manual
### Clonar el Repositorio
```bash
git clone https://github.com/DAndersonBurga/filebrowser-backend.git
cd filebrowser
```

### Compilar el Proyecto
```shell
mvn clean install
```

### Ejecutar la Aplicación
```shell
mvn spring-boot:run
```

## Nota
El proyecto solo puede cargar un sistema de archivos a la vez y este es tratado como un singleton por lo que si se carga un nuevo sistema de archivos, el anterior será eliminado.