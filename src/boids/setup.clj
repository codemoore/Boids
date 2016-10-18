(ns boids.setup
   (:require [boids.utils :as util]
             [boids.globals :as globals]
             [quil.core :as q]))

(defn regen-boid-pos
   "regenerate starting position"
   [boid]
   (let [x (:x globals/screen-size)
         y (:y globals/screen-size)
         b (/ globals/boid-diam 2)]
      (assoc (util/generate-rand-position (+ 0 b) (- x b) (+ 0 b) (- y b))   ;; starting x, y pos
      :vel-x (:vel-x boid)
      :vel-y (:vel-x boid))))

(defn create-boid

   ([boid boids]
    "create a boid in a random location that is not inside another boid"
    (if (util/too-close? boids boid globals/personal-space)
       (create-boid (regen-boid-pos boid) boids)
       ;; else
       boid))

   ([]
    "create a boid in a random position"
    (let [x (:width globals/screen-size)
          y (:height globals/screen-size)
          heading-angle (util/generate-heading-angle)
          velocity (utils/get-velocity heading-angle)]
       (assoc (util/generate-rand-position 0 x 0 y)   ;; starting x, y pos
          :heading heading-angle        ;; global angle the boid is facing
          :vel-x ()    ;; starting velocities set to 0, velocity will be calculated on first update
          :vel-y 0)))

   ([boids]
   "create a boid in a random location that is not inside another boid"
   (let [boid (create-boid)]
      (create-boid boid boids))))



(defn generate-boids
   ([boids n]
   "recursive call, create a seq of n boids"
   (if (> n 0)
      (generate-boids (conj boids (create-boid boids)) (- n 1))
      boids))

   ([]
   "create vector of n-boids"
    (generate-boids [] globals/n-boids)))

(defn setup
   []
   "Set initial quil params and return intial state"
   (q/frame-rate 5)
   {:boids (generate-boids)
    :boid-diam globals/boid-diam
    :boid-speed globals/boid-speed
    :frame 0})








