### Animaci�n

Ejecutar animation.m , que tiene los siguientes par�metros:

* **M**: Tama�o de la matriz.
* **particle**: id de la part�cula cuyos vecinos se quieren ver.
* **static_file**: Path al archivo de valores estaticos.
* **dynamic_file**: Path al archivo de valores din�micos.
* **output_file**: Path al archivo que contiene el output de la simulaci�n.


animation(5,200,'../static.txt','../dynamic.txt','../adjacent.txt');