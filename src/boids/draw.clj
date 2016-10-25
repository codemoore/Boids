(ns boids.draw
   (:require [quil.core :as q]
             [boids.globals :as globals]
             [boids.utils :as util]))

(defn draw-boid
   [boid diameter]
   "draw a single boid"
   (apply q/fill globals/boid-fill-color)
   (apply q/stroke globals/boid-stroke-color)
   (q/stroke-weight globals/boid-stroke-weight)
   (let [x (/ (:width globals/screen-size) 2)
         y (/ (:height globals/screen-size) 2)
         bx (:x (:pos boid))
         by (:y (:pos boid))
         r (/ diameter 2)
         #_(a (* radius (/ (Math/sqrt 2) 2))) ]
      (q/push-matrix) ;; save previous state
      (q/translate bx by) ;; move origin to boid position
      (q/line 0 0 (* (:x (:vel boid)) diameter) (* (:y (:vel boid)) diameter))
      (q/stroke 0)
      (q/rotate  (util/calc-angle (:vel boid)) ) ;; rotate to direction of movement
      ;(q/triangle    0     (* r -2)
      ;            (* r -1) (* r 2)
      ;               r     (* r 2))
      (q/line (- 0 r) (+ 0 r)
              (+ 0 r) (- 0 r))
      (q/pop-matrix))) ;; return to previous state

(defn draw-boids
   [state]
   "loop through boids and draw them"
   (let [diam (:boid-diam state)]
      (loop [boids (:boids state)]
         (when (not-empty boids)
            (draw-boid (first boids) diam)
            (recur (rest boids))))))

(defn draw-state
   [state]
   "draws to screen based on [state]"
   (q/clear)
   (apply q/background globals/background-color)
   (draw-boids state))



