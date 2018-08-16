/**
* Simple Oscillator
*/
life3D.set(1, 1, 9);
life3D.set(2, 1, 9);
life3D.set(3, 1, 9);
life3D.set(1, 3, 9);
life3D.set(2, 3, 9);
life3D.set(3, 3, 9);
life3D.set(1, 1, 9);
life3D.set(1, 2, 9);
life3D.set(1, 3, 9);
life3D.set(3, 1, 9);
life3D.set(3, 2, 9);
life3D.set(3, 3, 9);

life3D.set(1, 1, 10);
life3D.set(2, 1, 10);
life3D.set(3, 1, 10);
life3D.set(1, 3, 10);
life3D.set(2, 3, 10);
life3D.set(3, 3, 10);
life3D.set(1, 1, 10);
life3D.set(1, 2, 10);
life3D.set(1, 3, 10);
life3D.set(3, 1, 10);
life3D.set(3, 2, 10);
life3D.set(3, 3, 10);


// Rule 4555 is stable, according to http://web.stanford.edu/~cdebs/GameOfLife/
life3D.run(200, Life3D.wxyzRuleFactory(6, 7, 6, 7));