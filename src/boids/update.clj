(ns boids.update
   (:require [boids.utils :as util]
             [boids.globals :as globals]
             [boids.steer :as steer]))


(defn move
   ([pos vel speed]
    "
      move based of vel * speed,
      overflow to start
    "
    {:x (mod (+ (:x pos) (* (:x vel) speed)) (:width globals/screen-size))
     :y (mod (+ (:y pos) (* (:y vel) speed)) (:height globals/screen-size))}))

(defn update-boid
   [boid boids speed]
   "
      Update boid position based on velocity
      Update velocity and heading angle based on other boids within influence distance
   "
   (let [steering (steer/steer-boid boid boids [])
         new-velocity (:vel steering)]
        {:pos (move (:pos boid) new-velocity speed)
         :vel new-velocity
         :heading (:heading steering)}))

(defn update-boids
   [state]
   "loop through and update all boids"
   (loop [old-boids (:boids state)
          new-boids []]
      (if (empty? old-boids)
         (do new-boids)
         (do (let [new-boids (conj new-boids
                                   (update-boid (first old-boids)
                                    (:boids state)
                                    (:boid-speed state)))]
                  (recur (rest old-boids) new-boids))))))



(defn update-boid-speed
   [state]
   ""
   (:boid-speed state))

(defn update-targets [state]
   "if there are boids on target remove target"
   (let [boids (:boids state)
         targets (:targets state)
         dist (+ globals/target-boid-dist (/ globals/bird-seed-diam 2)) ]
      (if (> (count targets) 0)
         (filter
            #(not (util/too-close-boids? boids {:pos %} dist))
            targets)
         [])))

(defn update-state
   [state]
   "returns updated state"
   {:boids (update-boids state)
    :targets (update-targets state)
    :boid-diam (:boid-diam state)
    :boid-speed (update-boid-speed state)
    :frame (inc (or (:frame state) 0))})