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
   [boids boid dist]
   "
   is [boid] within [dist] distance from another boid in [boids]?
   "
   (some #(< (distance boid %) dist) boids))

(defn in-range?
   [boid1 boid2]
   "Is boid2 in range of boid1?"
   (<= (distance boid1 boid2) globals/infl-distance))

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

(defn get-vx
   [theta]
   "
      use angle theta in radians to calulate the unit vx
   "
   (let [vx (/ (Math/tan theta) (Math/sqrt (+ (Math/pow (Math/tan theta) 2) 1)))]
      (if (> theta 180) ;; if the angle is greater than 180 x will be negative
         (* -1 vx)
         vx)))

(defn get-vy
   [vx theta]
   "
      get vy in terms of vx, use theta in radians for polarity
   "
   (let [vy (Math/sqrt (- 1 (Math/pow vx 2)))]
      (if (or (< theta 90) (> theta 270)) ;; theta 0-90 or 270 - 360 means
         (* -1 vy)
         vy)))

(defn get-unit-vector
   [heading-angle]
   "
      calculate and return unit-vector-velocity (vx + vy = 1) based on [heading-angle]
      straight up (vx=0 vy=-1) is 0 deg, right (vx=1, vy=0) is 90 deg, etc
   "
   (let [x (get-vx heading-angle)
         y (get vy x)]))
