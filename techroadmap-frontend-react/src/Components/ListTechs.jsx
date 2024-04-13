import React, { useEffect, useState } from 'react'
import { techList } from '../Services/TechsService';
import '../CSS/ListTech.css';
import { HomePageContent } from './Paragraphs';
import FooterComponent from './FooterComponent';

function ListTechs() {

    const [listOfTechs, setListOfTechs] = useState([]);

    useEffect(() => {
        techList().then((response) => {
            const techs = Object.values(response.data);
            console.log(techs);
            setListOfTechs(techs);
        }).catch(error => {
            console.error(error);
        })
    }, []);

    return (
        <div className='container'>
            <div style={{ width: '20%', float: 'left', marginTop: '20px' }}>
                <table style={{ width: '100%' }} className='table table-striped table-bordered'>
                    <thead>
                        <tr>
                            <th>Technologies</th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            listOfTechs.map((tech, index) =>
                                <tr key={index}>
                                    <td>{tech}</td>
                                </tr>
                            )
                        }
                    </tbody>
                </table>
            </div>
            <div style={{ marginLeft: '-35%', marginTop: '20px' }}>
                <HomePageContent />
            </div>
            <div style={{ clear: 'both' }}></div> {/* Clear the float */}
        </div>
    
    );
}

export default ListTechs
