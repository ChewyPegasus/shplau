import Header from "./components/Header"
import Footer from "./components/Footer"
import React from "react";
import Items from "./components/Items";
import Categories from "./components/Categories";

class App extends React.Component{
  constructor(props) {
    super(props)
    this.state = {
      orders: [],
      currentItems: [],
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
          category: "rent",
          price: "200",
        },
        {
          id: 3,
          title: "Сидушка",
          img: "sidushka.jpg",
          desc: "For greater comfort",
          category: "rent",
          price: "10",
        },
        {
          id: 4,
          title: "Готовка еды",
          img: "food.jpg",
          desc: "Delicious menu",
          category: "work",
          price: "100",
        },
        {
          id: 5,
          title: "Спасатель на сплаве",
          img: "rescuer.JPG",
          desc: "For help during kayaking",
          category: "work",
          price: "70",
        },
        {
          id: 6,
          title: "Шашлык",
          img: "shash.png",
          desc: "Tasty pork meat",
          category: "food",
          price: "20",
        }
      ]
    }

    this.state.currentItems = this.state.items
    this.addToOrder = this.addToOrder.bind(this)
    this.deleteOrder = this.deleteOrder.bind(this)
    this.chooseCategory = this.chooseCategory.bind(this)
  }

  render() {
    return (
      <div className="wrapper">
        <Header orders={this.state.orders} onDelete={this.deleteOrder}/>
        <Categories chooseCategory={this.chooseCategory}/>
        <Items items={this.state.currentItems} onAdd={this.addToOrder}/>
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

  chooseCategory (category) {
    console.log(category)
  }
}


export default App;
