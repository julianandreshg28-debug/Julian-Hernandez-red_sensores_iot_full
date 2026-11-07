# Criterios de Decisión: Estructuras de Datos en la Red de Sensores

## 1. El dilema del historial: ArrayList vs. LinkedList

Si el sistema registra miles de eventos por segundo y necesita eliminar constantemente los más antiguos, elegiría `LinkedList`, ya que permite eliminar el primer elemento en tiempo O(1). En cambio, en un `ArrayList`, eliminar el primer elemento requiere desplazar los demás registros, por lo que la operación tiene una complejidad O(n).

Sin embargo, en nuestro proyecto utilizamos `ArrayList` porque permite acceder rápidamente a los eventos y agregar registros al final de manera eficiente. Además, implementamos `subList().clear()` para eliminar varios eventos antiguos en una sola operación, evitando eliminarlos individualmente.

Por eso, considero que ambas estructuras pueden ser útiles, pero su elección depende de las operaciones que realice el sistema con mayor frecuencia.

## 2. Navegación bidireccional: Deque vs. Queue

La principal ventaja de `Deque` es que permite agregar y retirar elementos por ambos extremos, mientras que una `Queue` tradicional se utiliza principalmente para procesar elementos desde el frente siguiendo el orden FIFO.

Esto resulta útil para gestionar las alertas porque necesitamos desplazarnos hacia adelante y hacia atrás.

Se relaciona con el concepto de `NodoDoble` porque un nodo doble contiene una referencia al siguiente nodo y otra al anterior, permitiendo recorrer una estructura en ambas direcciones.

En nuestro proyecto utilizamos `ArrayDeque` para almacenar las alertas y una variable que controla la posición actual. De esta manera podemos seleccionar la alerta anterior o siguiente. Aunque `ArrayDeque` no utiliza necesariamente nodos dobles internamente, permite trabajar con operaciones propias de una cola de doble extremo.

## 3. El ciclo de monitoreo: Queue vs. Stack

Una `Queue` es adecuada para el monitoreo porque funciona siguiendo el principio FIFO: el primer sensor que entra es el primero que se consulta.

En nuestro programa, cuando se consulta un sensor, este se retira del frente de la cola y se vuelve a agregar al final. Así todos los sensores tienen la oportunidad de ser consultados de manera ordenada y repetitiva.

Si utilizáramos una `Stack`, el funcionamiento sería diferente porque sigue el principio LIFO, donde el último elemento agregado es el primero en salir.

Esto podría afectar la equidad del monitoreo, ya que los sensores agregados recientemente podrían recibir atención antes que los que llevan más tiempo esperando. Por esa razón, la cola es más adecuada para nuestro sistema.

## 4. Topología de red: Lista de adyacencia vs. Matriz de adyacencia

Elegimos `Map<String, List<String>>` porque nuestra red de sensores tiene relativamente pocas conexiones por estación.

Una matriz de adyacencia necesita reservar espacio para representar todas las posibles conexiones entre sensores, incluso cuando muchas de ellas no existen. Su costo de memoria es O(V²), donde V representa el número de sensores.

En cambio, una lista de adyacencia almacena únicamente los sensores y sus conexiones existentes. Su costo de almacenamiento es O(V + E), donde E representa el número de conexiones.

En nuestro proyecto, el `Map` permite identificar cada sensor mediante su código y asociarlo con una lista que contiene sus vecinos.

Esto se relaciona con el concepto de `NodoGrafo`, porque cada sensor representa un vértice y su lista de vecinos representa las conexiones que tiene con otros vértices.

Además, utilizamos `HashMap`, que permite buscar un sensor por su identificador en tiempo O(1) promedio.

Por estas razones, la lista de adyacencia es una alternativa más eficiente para representar nuestra red.

## 5. Buffer de ingesta: ArrayDeque vs. ConcurrentLinkedQueue

En nuestro proyecto utilizamos `ArrayDeque` para almacenar y procesar las lecturas de los sensores en orden FIFO.

Esta estructura es adecuada cuando las operaciones se realizan de manera secuencial, porque permite agregar elementos al final y retirarlos del frente de forma eficiente.

Sin embargo, si el sistema aumentara considerablemente su volumen de datos y necesitara recibir o procesar lecturas desde varios hilos al mismo tiempo, sería más conveniente utilizar `ConcurrentLinkedQueue`.

La razón es que `ArrayDeque` no es una estructura segura para el acceso concurrente sin sincronización externa. Si varios hilos la modifican simultáneamente, podrían producirse inconsistencias.

En cambio, `ConcurrentLinkedQueue` está diseñada para permitir operaciones concurrentes de forma segura, sin utilizar un bloqueo global para cada operación.

Por lo tanto, mantendría `ArrayDeque` para el funcionamiento actual, pero consideraría `ConcurrentLinkedQueue` si el sistema necesitara trabajar con múltiples hilos.