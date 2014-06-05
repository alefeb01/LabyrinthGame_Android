     				Project Labyrinth Ball
     
     
    v1 Labyrinth Core
    
    v2.1
    OOP : Door new object, Portal (teleportation of the ball)
    to improve : memory not optimum  save
    next stage : Gate and Trigger
    
     v2.2 
    OOP : Gate&Trigger ready : the trigger open the gate
    
    v2.2.1 correct lvl 10 & 11
    
    v2.2.2
    Remove global WORLD_DEV_LIMIT and LVL_PER_WORLD, using assetmanager to get it through
    
    v2.3
    OOP : Multiple switches to reverse the behavior to the sensor (very fun trick)
    bug : when the speed of the ball is too high it can go through the walls..
    
    LabX.txt
    s = start bloc
    a = arrival block
    r/q = switch r would be the switch active during the initiation
    h = hole
    b = bloc (wall)
    i1i1i1j14 = i for in o for out (Portal in/out) j and p represent last bloc , 1 is the door number, 4 front indication
    1 = Right 2= Left 3= Top 4 = Bottom  (maybe top/bottom are reverse...)
    g1g1g1 = gate 1 (gate as border 0 and lenght-1, during opening the other blocs change status)
    t1 = trigger 1 (circle shape, popup when triggered and disappear)
    
    
    MAPs = w0 complete 10 lvls learn to use the ball and avoid holes.
    	   w1 lvl 10 11, discover portals and gate&trigger
    	   	  lvl 12, discover Switch