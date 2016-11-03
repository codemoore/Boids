(ns boids.steer
   (:require [boids.utils :as util]
             [boids.globals :as globals]))

(defn get-boids-in-range
   [boid boids]
   "return seq of boids that are in influence range of [boid]"
   (doall (filter #(and (util/in-range? boid %) (not (identical? boid %))) boids)))

(defn avg-value [& args]
   (/ (apply + args)
      (count args)))

(defn avg-xy [& args]
   "average of seq of x,y pairs"
   {:x (apply avg-value (map #(:x %) args))
    :y (apply avg-value (map #(:y %) args))})

(defn get-avg-pos [boids]
   (apply avg-xy (map #(:pos %) boids)))

(defn get-avg-vel [boids]
   (apply avg-xy (map #(:vel %) boids)))

(defn apply-weight [vel weight]
   {:x (* (:x vel) weight)
    :y (* (:y vel) weight) })

(defn alignment
   [state]
   "
      Steer towards the average velocity of local flockmates
      use average velocities in [avg-boids]
      return average heading angle
   "
   (let [boids (:boids state)
         count (count boids)
         vel (get-avg-vel boids)
         ali (util/normalize-velocity vel)]
      (apply-weight ali globals/ali-weight)))

(defn cohesion
   [state]
   "
      steer to move toward the average position of local flockmates
      use [avg-boids] position to get and return vector that would move
      boid towards the average position
   "
   (let [boid (:boid state)
         boids (:boids state)
         avg-pos (get-avg-pos boids)
         avg-x (:x avg-pos)
         avg-y (:y avg-pos)
         x (:x (:pos boid))
         y (:y (:pos boid))
         coh (util/normalize-velocity {:x (/ (- avg-x x) 100)
                                       :y (/ (- avg-y y) 100)})]
         (apply-weight coh globals/coh-weight)))



(defn reduce-sep-fn [sym boid]
   #(- %1 (/ (- (sym (:pos %2)) (sym (:pos boid))) (util/distance (:pos boid) (:pos %2)))))

(defn seperation
   [state]
   "
      steer to avoid crowding local flockmates
      if there are one or more boids that are too close,
      return a velocity of the average of those boids
      else, return nil
   "
   (let [boid (:boid state)
         boids (:boids state)
         close-boids (filter #(util/too-close? boid %) boids)
         size (count close-boids)
         sep-vel (if (> size 0)
                    {:x (reduce (reduce-sep-fn :x boid) 0 close-boids)
                     :y (reduce (reduce-sep-fn :y boid) 0 close-boids)}
                    {:x 0 :y 0})
         sep (util/normalize-velocity sep-vel)]
      (apply-weight sep globals/sep-weight)))

(defn closest-target [boid targets]
   (reduce
      #(if (< (util/distance %2 (:pos boid)) %1)
           (%2)
           (%1)) targets))

(defn seek
   [state]
   "
      if there is a target to seek steer towards it
   "
   (let [targets (:targets state)
         boid (:boid state)
         ;; seek only to the closest target
         closest-target (if (> (count targets) 0)
                              (closest-target boid targets)
                              nil)]
      (apply-weight (if closest-target
                        (util/normalize-velocity {:x  (- (:x closest-target) (:x boid))
                                                   :y  (- (:y closest-target) (:y boid))})
                        {:x 0 :y 0})
                    globals/seek-weight)))

(defn self-vel [state]
   (let [boid (:boid state)]
      (apply-weight (:vel boid) (* globals/self-weight globals/boid-speed))))


(defn apply-rules [state & rules]
   "
      takes a set of 2 param rule fn's that apply a rule to [boid, boids] and return a velocity
      returns a seq of these velocities
   "
   (loop [results []
          rule-seq rules]
      (if (not-empty rule-seq)
         (let [rule (first rule-seq)
               vel (rule state)
               remaining (rest rule-seq)]
             (recur (conj results vel) remaining))
         results)))

(defn combine [p1 p2]
   {:x (+ (:x p1) (:x p2))
      :y (+ (:y p1) (:y p2))})

(defn steer-boid
   [boid boids targets]
   "return unit-velocity"
   (let [boids-in-range (get-boids-in-range boid boids)
         state {:boid boid
                :boids boids-in-range
                :targets targets}]
      (if (> (count boids-in-range) 0)
         (let [velocities (conj (apply-rules state alignment cohesion seperation seek self-vel)
                                (:vel boid))
               combined (reduce combine velocities)]
            ;; combine velocities and limit
            {:vel (util/limit-velocity (combine combined (:vel boid)))})
         boid))) ;; else return boid