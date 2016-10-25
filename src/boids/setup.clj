(ns boids.setup
   (:require [boids.utils :as util]
             [boids.globals :as globals]
             [quil.core :as q]))

(defn regen-boid-pos
      "regenerate starting position"
      [boid]
      (let [w (:width globals/screen-size)
         h (:height globals/screen-size)
         b (/ globals/boid-diam 2)]
           {:pos #_{:x (/ w 2) :y (/ h 2)} (util/generate-rand-position b (- w b) b (- h b))  ;; starting x, y pos
            :vel (:vel boid)
            :heading (:heading boid)}))

(defn create-boid

   ([boid boids]
    "create a boid in a random location that is not inside another boid"
    (if (util/too-close-boids? boids boid globals/personal-space)
       (create-boid (regen-boid-pos boid) boids)
       ;; else
       boid))

   ([]
    "create a boid in a random position"
    (let [b (/ globals/boid-diam 2)
          x (- (:width globals/screen-size) b)
          y (- (:height globals/screen-size) b)
          heading-angle (util/generate-heading-angle)
          velocity (util/get-unit-vector heading-angle)]
       {:pos (util/generate-rand-position b x b y)   ;; starting x, y pos
        :vel velocity}))

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
   (q/frame-rate globals/frame-rate)
   {:boids (generate-boids)
    :boid-diam globals/boid-diam
    :boid-speed globals/boid-speed
    :frame 0})








