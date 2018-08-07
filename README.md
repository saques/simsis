### Animación

Ejecutar animation.m , que tiene los siguientes parámetros:

* **M**: Tamaño de la matriz.
* **particle**: id de la partícula cuyos vecinos se quieren ver.
* **static_file**: Path al archivo de valores estaticos.
* **dynamic_file**: Path al archivo de valores dinámicos.
* **output_file**: Path al archivo que contiene el output de la simulación.


animation(5,200,'../static.txt','../dynamic.txt','../adjacent.txt');