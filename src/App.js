import Header from "./components/Header"
import Footer from "./components/Footer"
import React from "react";
import Items from "./components/Items";

class App extends React.Component{
  constructor(props) {
    super(props)
    this.state = {
      orders: [],
      items: [
        {
          id: 1,
          title: 'Байдарка',
          img: 'baidarka.jpg',
          desc: 'ProTour 470',
          category: 'stuff',
          price: "100",
        },
        {
          id: 2,
          title: "Беседка",
          img: "besedka.JPG",
          desc: "Lesxoz property",
          category: "service",
          price: "200",
        },
        {
          id: 3,
          title: "Сидушка",
          img: "sidushka.jpg",
          desc: "For greater comfort",
          category: "stuff",
          price: "10",
        }
      ]
    }

    this.addToOrder = this.addToOrder.bind(this)
    this.deleteOrder = this.deleteOrder.bind(this)
  }

  render() {
    return (
      <div className="wrapper">
        <Header orders={this.state.orders} onDelete={this.deleteOrder}/>
        <Items items={this.state.items} onAdd={this.addToOrder}/>
        <Footer />
      </div>
    );
  }

  addToOrder (item) {
    let isInOrders = false;
    this.state.orders.forEach(el => {
      if (el.id === item.id)
        isInOrders = true
    })
    if (!isInOrders)
      this.setState({orders: [...this.state.orders, item] })
  }

  deleteOrder (id) {
    this.setState({orders: this.state.orders.filter(el => el.id !== id)})
  }
}


export default App;
