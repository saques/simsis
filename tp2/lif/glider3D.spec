/**
         * 3D glider, 5655.
         * https://pdfs.semanticscholar.org/8eda/1c703dc143269c1613fcff63fee15f15f899.pdf
         */
        life3D.set(1, 0, 10);
        life3D.set(2, 0, 10);
        life3D.set(1, 1, 10);
        life3D.set(2, 1, 10);
        life3D.set(1, 3, 10);
        life3D.set(2, 3, 10);
        life3D.set(1, 4, 10);
        life3D.set(2, 4, 10);

        life3D.set(0, 2, 10);
        life3D.set(3, 2, 10);

        life3D.set(0, 1, 9);
        life3D.set(0, 3, 9);

        life3D.set(3, 1, 9);
        life3D.set(3, 3, 9);

        life3D.set(1, 0, 9);
        life3D.set(2, 0, 9);
        life3D.set(1, 4, 9);
        life3D.set(2, 4, 9);

        life3D.set(3, 1, 9);
        life3D.set(3, 3, 9);
        life3D.set(3, 1, 9);
        life3D.set(3, 3, 9);
        // Rule 4555 is stable, according to http://web.stanford.edu/~cdebs/GameOfLife/
        life3D.run(200, Life3D.wxyzRuleFactory(5, 6, 5, 5));