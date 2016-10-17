(ns boids.utils
   (:require [boids.globals :as globals]))

(defn distance
   [boid1 boid2]
   "
   absolute distance between 2 x, y coordinate pairs
   "
   (let [x1 (:x boid1)
         y1 (:y boid1)
         x2 (:x boid2)
         y2 (:y boid2)]
   (Math/sqrt (+ (Math/pow (- x2 x1) 2) (Math/pow (- y2 y1) 2)))))

(defn too-close?
   "
      is [boid] within [dist] distance from another boid in [boids]?

   "
   [boids boid dist]
   (some #(< (distance boid %) dist) boids))



(defn rand-bounded
   [min max]
   "return random number between min and max"
   (+ (rand (- max min)) min))

(defn generate-rand-position
   [x-min x-max y-min y-max]
   "
      return a random {:x :y} coordinate map,
      bounded by x-min x-max and y-min y-max
   "
   {:x (rand-bounded x-min x-max)
    :y (rand-bounded y-min y-max)})



(defn generate-heading-angle
   []
   "
     return starting heading
   "
   (let [min (- globals/heading-angle globals/heading-angle-dev)
         max (+ globals/heading-angle globals/heading-angle-dev)]
      (rand-bounded min max)))
