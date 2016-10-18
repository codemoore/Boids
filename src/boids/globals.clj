(ns boids.globals
   (:require [quil.core :as q]))

;;"Display title"
(def title "Boids")

;;"display screen size"
(def screen-size {:width 600
                  :height 600})

;;"Maxium distance in pixels from boid's center that another boid can influence their movement"
(def infl-distance 400)

;;"Boids should give this much space between themselves and other boids"
(def personal-space 10)

;;"Max angle from boid's movement direction that another boid can be to still influence movement"
(def infl-angle (/ Math/PI 2))

;;"Max movement speed for a boid, in pixels/frame"
(def boid-speed 10)

;;"Intial amount of boids spawned"
(def n-boids 10)

;;"Diameter size of boids"
(def boid-diam 10)

;; boid stroke weight
(def boid-stroke-weight 1)

;;"Intial heading angle"
(def heading-angle 0)

;;"Max deviation from heading angle"
(def heading-angle-dev (/ Math/PI 2))

;;"Boid fill color"
(def boid-fill-color [100 100 200])

;;"Boid stroke color"
(def boid-stroke-color [255 0 0])

;;"background color"
(def background-color [255 255 255])