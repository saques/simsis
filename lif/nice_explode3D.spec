/**
* Nice explosion.
* https://www.youtube.com/watch?v=EW9Q0qMc2Xc
*/

life3D.set(100,100, 100);
life3D.set(100,101, 100);
life3D.set(100,100, 101);
life3D.set(100,101, 101);
// Rule 4555 is stable, according to http://web.stanford.edu/~cdebs/GameOfLife/
life3D.run(100, Life3D.b4s9Rule.fabricate());