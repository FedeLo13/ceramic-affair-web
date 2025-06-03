import './About.css'

export default function About() {
    return (
        <div className="about-section">
            <div className="about-block">
                <img src="/images/About_1.png" alt="Ceramic Affair Image" className="about-image-1" />                           
            </div>

            <div className="about-block">
                <p className="about-text">
                    Ceramic Affair is a project dedicated to the art of ceramics, showcasing a collection of unique pieces that blend traditional craftsmanship with contemporary design. Each piece tells a story, reflecting the passion and creativity of its maker.
                </p>                            
            </div>

            <div className="about-block">
                <img src="/images/About_2.jpeg" alt="Ceramic Affair Image" className="about-image-2" />
            </div>  

            <div className="about-block">
                <p className="about-text">
                    Our mission is to promote the beauty and diversity of ceramic art, connecting artists with enthusiasts and collectors. We believe in the power of ceramics to inspire, educate, and bring people together.
                </p>                            
            </div>        
        </div>
    );
}