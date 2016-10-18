# boids

A Quil sketch designed to ... well, that part is up to you.

## TODO
    - Give boids a intial velocity
    - Rework heading
        - remove initial random heading, calculate base on velocity
        - Decide whether to store heading in state (probably not)
    - Finish steering logic (maybe put in new namespace)
    -Change heading angle to radians

## Usage

LightTable - open `core.clj` and press `Ctrl+Shift+Enter` to evaluate the file.

Emacs - run cider, open `core.clj` and press `C-c C-k` to evaluate the file.

REPL - run `(require 'boids.core)`.

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
