(ns boids.utils
   (:require [boids.globals :as globals]))

(defn distance
   [pos1 pos2]
   "
   absolute distance between 2 x, y coordinate pairs
   "
   (let [x1 (:x pos1)
         y1 (:y pos1)
         x2 (:x pos2)
         y2 (:y pos2)]
      (if (and x1 y1 x2 y2)
         (Math/sqrt (+ (Math/pow (- x2 x1) 2) (Math/pow (- y2 y1) 2)))
         nil)))

(defn too-close-fn [boid dist]
   "2 single boids too close? fn"
   #(let [d (distance (:pos boid) (:pos %))]
      (and d (< d dist) (not (identical? boid %)))))

(defn too-close?
   [boid1 boid2]
   "2 single boids too close?"
   ((too-close-fn boid1 globals/personal-space) boid2))

(defn too-close-boids?
   ([boids boid dist]
   "
      is [boid] within [dist] distance from another boid in [boids]?
   "
   (some (too-close-fn boid dist) boids))

   ([boids boid]
   "
      use default value for dist
   "
   (too-close-boids? boids boid globals/personal-space)))

(defn in-range?
   [boid1 boid2]
   "Is boid2 in range of boid1?"
   (<= (distance (:pos boid1) (:pos boid2)) globals/infl-distance))

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
         y (get-vy x heading-angle)]
         {:x x :y y}))

(defn calc-angle [velocity]
   "Calculate heading angle in radians from velocity"
   (if (and velocity (:x velocity) (:y velocity))
      (let [ theta (Math/atan (/ (:x velocity) (* -1 (:y velocity))))]
         (if (neg? (:x velocity))
            (+ theta (/ Math/PI 2))
            theta))
      nil))

(defn normalize-velocity [vel]
   "velocity should be limited to a unit vector where abs(x) + abs(y) <= 1
   normalizes vector"
   (let [x-old (:x vel)
         y-old (:y vel)
         magnitude (Math/sqrt (+ (Math/pow x-old 2) (Math/pow y-old 2)))]
      (if (> magnitude 0)
         {:x (/ x-old magnitude)
          :y (/ y-old magnitude)}
         vel)))

