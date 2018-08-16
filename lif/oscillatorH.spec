/**
* Simple H Oscillator.
* http://wpmedia.wolfram.com/uploads/sites/13/2018/02/06-5-3.pdf
*/
life3D.set(1, 1, 10);
life3D.set(1, 2, 10);
life3D.set(1, 3, 10);
life3D.set(2, 2, 10);
life3D.set(3, 1, 10);
life3D.set(3, 2, 10);
life3D.set(3, 3, 10);

// Rule 4555 is stable, according to http://web.stanford.edu/~cdebs/GameOfLife/
life3D.run(200, Life3D.wxyzRuleFactory(5, 6, 5, 5));