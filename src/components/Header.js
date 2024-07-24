import React, { useState } from 'react'
import { FaCartShopping } from "react-icons/fa6";

export default function Header() {
  let [cartOpen, setCartOpen] = useState(false)

  return (
    <header>
        <div>
            <span className='logo'>Tourist Stuff</span>
            <ul className='nav'>
                <li>About us</li>
                <li>Contact</li>
                <li>Profile</li>
            </ul>
            <FaCartShopping onClick={() => setCartOpen(cartOpen = !cartOpen)} className={`shop_cart_button ${cartOpen && 'active'}`} />
            {cartOpen && (
              <div className='shop_cart'>
              </div>
            )}
        </div>
        <div className='presentation'></div>
    </header>
  )
}
